/*
 * SENTINELFLOW ENTERPRISE SCHEMA
 * ------------------------------------------------------------------
 * Features:
 * 1. Advanced RBAC (Role-Based Access Control)
 * 2. Subscription Management (Free/Premium/Enterprise)
 * 3. Soft Deletes (deleted_at) for data recovery
 * 4. Immutable Audit Logs with JSONB
 * 5. Strict Foreign Key Constraints & Cascading
 * 6. High-Performance Indexing Strategy
 */

-- ==================================================================
-- 1. EXTENSIONS & CONFIGURATIONS
-- ==================================================================
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto"; -- For advanced hashing if needed

-- ==================================================================
-- 2. ENUMS (Type Safety & State Machines)
-- ==================================================================
-- User Roles: Hierarchical permission structure
CREATE TYPE user_role AS ENUM ('MEMBER', 'PREMIUM', 'MODERATOR', 'ADMIN');

-- Account Status: For banning/suspending users
CREATE TYPE account_status AS ENUM ('ACTIVE', 'SUSPENDED', 'BANNED', 'PENDING_VERIFICATION');

-- Transaction States: Strict financial state machine
CREATE TYPE transaction_status AS ENUM ('PENDING', 'COMPLETED', 'FAILED', 'CANCELLED', 'REFUNDED');
CREATE TYPE transaction_type AS ENUM ('INCOME', 'EXPENSE', 'TRANSFER');

-- Audit Actions: What happened?
CREATE TYPE audit_action AS ENUM ('CREATE', 'UPDATE', 'DELETE', 'LOGIN', 'LOGOUT', 'EXPORT_DATA');

-- ==================================================================
-- 3. CORE IDENTITY & ACCESS (RBAC)
-- ==================================================================

-- A. USERS TABLE
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    -- Identity
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    avatar_url TEXT,

    -- Access Control
    role user_role NOT NULL DEFAULT 'MEMBER',
    status account_status NOT NULL DEFAULT 'ACTIVE',

    -- Security Metadata
    last_login_at TIMESTAMP WITH TIME ZONE,
    failed_login_attempts INT DEFAULT 0,
    requires_2fa BOOLEAN DEFAULT FALSE,

    -- Timestamps & Soft Delete
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE -- NULL means active, Date means "Trash Bin"
);

-- B. SUBSCRIPTIONS (Monetization Layer)
-- Tracks if a user is Free vs. Premium
CREATE TABLE IF NOT EXISTS subscriptions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    tier VARCHAR(50) NOT NULL DEFAULT 'FREE', -- FREE, PRO, ENTERPRISE
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, PAST_DUE, CANCELED

    start_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    next_billing_date TIMESTAMP WITH TIME ZONE,
    stripe_customer_id VARCHAR(100), -- For future Stripe integration

    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- C. REFRESH TOKENS (JWT Security)
CREATE TABLE IF NOT EXISTS refresh_tokens (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token_hash VARCHAR(512) NOT NULL, -- Store hash, not raw token for extra security
    device_info VARCHAR(255), -- "Chrome on Linux", "Pixel 7 App"
    ip_address INET,          -- Track where the login came from
    expiry_date TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ==================================================================
-- 4. FINANCIAL CORE
-- ==================================================================

-- D. CATEGORIES (Normalized)
CREATE TABLE IF NOT EXISTS categories (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE, -- NULL = System Category

    name VARCHAR(100) NOT NULL,
    type transaction_type NOT NULL DEFAULT 'EXPENSE',
    color_hex VARCHAR(7) DEFAULT '#808080', -- For UI charts
    icon_slug VARCHAR(50), -- e.g., "fa-coffee"

    parent_category_id UUID REFERENCES categories(id) ON DELETE SET NULL, -- Allow nested categories
    is_archived BOOLEAN DEFAULT FALSE,

    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, name, type) -- Prevent duplicates
);

-- E. BUDGETS (Smart Limits)
CREATE TABLE IF NOT EXISTS budgets (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    category_id UUID REFERENCES categories(id) ON DELETE SET NULL,

    name VARCHAR(100), -- "Monthly Groceries"
    amount_limit DECIMAL(19, 4) NOT NULL,
    current_spend DECIMAL(19, 4) DEFAULT 0.00, -- Cached for performance (optional)

    period VARCHAR(20) DEFAULT 'MONTHLY',
    alert_threshold_percent INT DEFAULT 85, -- Alert user when they hit 85%

    start_date DATE NOT NULL,
    end_date DATE, -- NULL means recurring indefinitely

    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- F. TRANSACTIONS (High-Volume Data)
CREATE TABLE IF NOT EXISTS transactions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    category_id UUID REFERENCES categories(id) ON DELETE SET NULL,

    -- Financial Data
    amount DECIMAL(19, 4) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    exchange_rate DECIMAL(19, 6) DEFAULT 1.0, -- Multi-currency support

    -- Context
    description VARCHAR(255),
    merchant_name VARCHAR(100),
    notes TEXT,

    -- State
    status transaction_status NOT NULL DEFAULT 'COMPLETED',
    type transaction_type NOT NULL DEFAULT 'EXPENSE',

    -- Recurrence (Optional)
    is_recurring BOOLEAN DEFAULT FALSE,
    recurring_frequency VARCHAR(20), -- MONTHLY, WEEKLY

    -- Attachments (Receipts)
    receipt_url TEXT,

    -- Timestamps
    transaction_date TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE -- Soft delete
);

-- ==================================================================
-- 5. AUDIT & COMPLIANCE (The "Sentinel" Part)
-- ==================================================================

-- G. AUDIT LOGS (Immutable)
-- Uses JSONB to store the exact "Diff" of what changed.
CREATE TABLE IF NOT EXISTS audit_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),

    -- Who did it?
    actor_id UUID REFERENCES users(id) ON DELETE SET NULL,
    ip_address INET,
    user_agent TEXT,

    -- What changed?
    entity_name VARCHAR(50) NOT NULL, -- 'TRANSACTION', 'USER', 'BUDGET'
    entity_id UUID NOT NULL,
    action audit_action NOT NULL,

    -- The Evidence
    old_values JSONB, -- Snapshot before change
    new_values JSONB, -- Snapshot after change

    timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- ==================================================================
-- 6. PERFORMANCE & INDEXING
-- ==================================================================

-- Identity Lookups
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);

-- Transaction Reporting (Critical for Dashboard Speed)
CREATE INDEX IF NOT EXISTS idx_tx_user_date ON transactions(user_id, transaction_date DESC);
CREATE INDEX IF NOT EXISTS idx_tx_category ON transactions(category_id);
CREATE INDEX IF NOT EXISTS idx_tx_status ON transactions(status);

-- Budget Tracking
CREATE INDEX IF NOT EXISTS idx_budgets_user ON budgets(user_id);

-- Audit Searching
CREATE INDEX IF NOT EXISTS idx_audit_entity ON audit_logs(entity_id);
CREATE INDEX IF NOT EXISTS idx_audit_actor ON audit_logs(actor_id);

-- ==================================================================
-- 7. TRIGGERS & AUTOMATION
-- ==================================================================

-- Auto-update 'updated_at' timestamp
CREATE OR REPLACE FUNCTION trigger_set_timestamp()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS set_timestamp_users ON users;
CREATE TRIGGER set_timestamp_users
BEFORE UPDATE ON users
FOR EACH ROW EXECUTE FUNCTION trigger_set_timestamp();

DROP TRIGGER IF EXISTS set_timestamp_transactions ON transactions;
CREATE TRIGGER set_timestamp_transactions
BEFORE UPDATE ON transactions
FOR EACH ROW EXECUTE FUNCTION trigger_set_timestamp();

DROP TRIGGER IF EXISTS set_timestamp_budgets ON budgets;
CREATE TRIGGER set_timestamp_budgets
BEFORE UPDATE ON budgets
FOR EACH ROW EXECUTE FUNCTION trigger_set_timestamp();

-- ==================================================================
-- 8. SECURITY POLICIES (RLS)
-- ==================================================================
-- Enable RLS on all sensitive tables
ALTER TABLE users ENABLE ROW LEVEL SECURITY;
ALTER TABLE transactions ENABLE ROW LEVEL SECURITY;
ALTER TABLE budgets ENABLE ROW LEVEL SECURITY;
ALTER TABLE audit_logs ENABLE ROW LEVEL SECURITY;

-- 1. Users can only see their own profile
DROP POLICY IF EXISTS "Users view own profile" ON users;
CREATE POLICY "Users view own profile" ON users
    FOR SELECT USING (auth.uid() = id);

-- 2. Admins can view all profiles
DROP POLICY IF EXISTS "Admins view all profiles" ON users;
CREATE POLICY "Admins view all profiles" ON users
    FOR ALL USING (
        EXISTS (SELECT 1 FROM users WHERE id = auth.uid() AND role = 'ADMIN')
    );

-- 3. Transactions: Owner OR Admin access
DROP POLICY IF EXISTS "Access own transactions" ON transactions;
CREATE POLICY "Access own transactions" ON transactions
    FOR ALL USING (
        user_id = auth.uid() OR
        EXISTS (SELECT 1 FROM users WHERE id = auth.uid() AND role IN ('ADMIN', 'MODERATOR'))
    );

-- ==================================================================
-- 9. SEED DATA (System Defaults)
-- ==================================================================

-- System Categories (Global)
INSERT INTO categories (name, type, color_hex, icon_slug) VALUES
('Salary', 'INCOME', '#2ecc71', 'wallet'),
('Investments', 'INCOME', '#27ae60', 'chart-line'),
('Rent / Mortgage', 'EXPENSE', '#e74c3c', 'home'),
('Groceries', 'EXPENSE', '#f1c40f', 'shopping-cart'),
('Utilities', 'EXPENSE', '#3498db', 'bolt'),
('Entertainment', 'EXPENSE', '#9b59b6', 'film'),
('Healthcare', 'EXPENSE', '#e67e22', 'heartbeat')
ON CONFLICT DO NOTHING;