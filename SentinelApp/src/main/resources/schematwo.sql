-- =================================================================
-- 1. SECURITY HARDENING (Enable RLS)
-- =================================================================

-- Enable RLS on all existing tables
ALTER TABLE public.users ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.transactions ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.subscriptions ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.support_tickets ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.audit_logs ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.categories ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.budgets ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.biometric_keys ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.chat_messages ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.private_messages ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.refresh_tokens ENABLE ROW LEVEL SECURITY;

-- =================================================================
-- 2. SCHEMA ENHANCEMENTS (Match UI Features)
-- =================================================================

-- 2.1 Enhancing USERS table for Profile & AI Settings
ALTER TABLE public.users
ADD COLUMN IF NOT EXISTS phone_number text,
ADD COLUMN IF NOT EXISTS settings jsonb DEFAULT '{"notifications": true, "theme": "dark", "ai_enabled": true}'::jsonb,
ADD COLUMN IF NOT EXISTS last_active_at timestamp with time zone;

-- 2.2 Enhancing TRANSACTIONS for "Live Feed" & AI Analysis
ALTER TABLE public.transactions
ADD COLUMN IF NOT EXISTS location character varying,
ADD COLUMN IF NOT EXISTS risk_score numeric DEFAULT 0.0, -- For AI Fraud Detection
ADD COLUMN IF NOT EXISTS ai_category_prediction character varying, -- What the AI thinks the category is
ADD COLUMN IF NOT EXISTS processed_at timestamp with time zone; -- For exact ordering in live feed

-- 2.3 New Table: NOTIFICATIONS (For the Bell Icon & Sidebar)
CREATE TABLE IF NOT EXISTS public.notifications (
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  user_id uuid NOT NULL REFERENCES public.users(id) ON DELETE CASCADE,
  title text NOT NULL,
  message text NOT NULL,
  type text NOT NULL CHECK (type IN ('ALERT', 'INFO', 'SUCCESS', 'WARNING')),
  is_read boolean DEFAULT false,
  created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
  action_link text, -- If clicking takes you somewhere
  CONSTRAINT notifications_pkey PRIMARY KEY (id)
);

-- 2.4 New Table: AI_INSIGHTS (For "SentinelAI Assistant")
CREATE TABLE IF NOT EXISTS public.ai_insights (
  id uuid NOT NULL DEFAULT uuid_generate_v4(),
  user_id uuid NOT NULL REFERENCES public.users(id) ON DELETE CASCADE,
  context_type text NOT NULL, -- e.g., 'SPENDING_SPIKE', 'FORECAST'
  content text NOT NULL, -- The chat message or insight
  related_transaction_id uuid REFERENCES public.transactions(id),
  created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
  is_dismissed boolean DEFAULT false,
  CONSTRAINT ai_insights_pkey PRIMARY KEY (id)
);

-- Enable RLS on new tables
ALTER TABLE public.notifications ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.ai_insights ENABLE ROW LEVEL SECURITY;

-- =================================================================
-- 3. RLS POLICIES (The "Basic Security" you were missing)
-- =================================================================

-- USERS: Users can read their own profile. Admins can read all.
CREATE POLICY "Users can read own profile" ON public.users
FOR SELECT USING (auth.uid() = id);

CREATE POLICY "Admins can read all profiles" ON public.users
FOR SELECT USING (
  EXISTS (SELECT 1 FROM public.users WHERE id = auth.uid() AND role = 'ADMIN')
);

-- TRANSACTIONS: Users see only their own transactions.
CREATE POLICY "Users see own transactions" ON public.transactions
FOR SELECT USING (auth.uid() = user_id);

CREATE POLICY "Users insert own transactions" ON public.transactions
FOR INSERT WITH CHECK (auth.uid() = user_id);

CREATE POLICY "Users update own transactions" ON public.transactions
FOR UPDATE USING (auth.uid() = user_id);

CREATE POLICY "Users delete own transactions" ON public.transactions
FOR DELETE USING (auth.uid() = user_id);

-- SUPPORT TICKETS: Users see own. Admins/Mods see all.
CREATE POLICY "Users see own tickets" ON public.support_tickets
FOR SELECT USING (auth.uid() = user_id);

CREATE POLICY "Staff see all tickets" ON public.support_tickets
FOR SELECT USING (
  EXISTS (SELECT 1 FROM public.users WHERE id = auth.uid() AND role IN ('ADMIN', 'MODERATOR'))
);

CREATE POLICY "Users create tickets" ON public.support_tickets
FOR INSERT WITH CHECK (auth.uid() = user_id);

-- NOTIFICATIONS & AI: Strictly owner only
CREATE POLICY "Owner sees notifications" ON public.notifications
FOR ALL USING (auth.uid() = user_id);

CREATE POLICY "Owner sees AI insights" ON public.ai_insights
FOR ALL USING (auth.uid() = user_id);

-- =================================================================
-- 4. PERFORMANCE TUNING (For "Live Monitor" Speed)
-- =================================================================

-- Index for the "Recent Activity" feed (Sort by date fast)
CREATE INDEX IF NOT EXISTS idx_transactions_user_date ON public.transactions(user_id, transaction_date DESC);

-- Index for Filtering by Category (Sidebar Filters)
CREATE INDEX IF NOT EXISTS idx_transactions_category ON public.transactions(user_id, category_id);

-- Index for unread notifications (Bell Icon count)
CREATE INDEX IF NOT EXISTS idx_notifications_unread ON public.notifications(user_id) WHERE is_read = false;

-- =================================================================
-- 5. TRIGGERS (Automated Logic)
-- =================================================================

-- Automatically create a User Profile in 'public.users' when someone signs up via Supabase Auth
CREATE OR REPLACE FUNCTION public.handle_new_user()
RETURNS trigger AS $$
BEGIN
  INSERT INTO public.users (id, email, username, role, status)
  VALUES (
    new.id,
    new.email,
    split_part(new.email, '@', 1), -- Default username from email
    'MEMBER',
    'ACTIVE'
  );
  RETURN new;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- Bind the trigger to the Auth table (if not exists)
DROP TRIGGER IF EXISTS on_auth_user_created ON auth.users;
CREATE TRIGGER on_auth_user_created
  AFTER INSERT ON auth.users
  FOR EACH ROW EXECUTE PROCEDURE public.handle_new_user();