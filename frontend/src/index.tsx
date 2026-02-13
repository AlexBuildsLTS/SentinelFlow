import React, { useState, useEffect, useRef } from "react";
import { createRoot } from "react-dom/client";
import {
  Activity,
  LayoutDashboard,
  Settings,
  Wallet,
  Bell,
  Search,
  Menu,
  X,
  Plus,
  ArrowUpRight,
  CheckCircle2,
  AlertCircle,
  Zap,
  User,
  LogOut,
  List,
  ShieldCheck,
  Smartphone,
  Wifi,
  Globe,
  Lock,
  Mail,
  Eye,
  EyeOff,
  KeyRound,
  ChevronRight,
  Filter,
  Calendar,
  RefreshCw,
} from "lucide-react";
import { createClient } from "@supabase/supabase-js";
import {
  AreaChart,
  Area,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from "recharts";

// --- Configuration ---
// Linked to your Supabase Project: puqqnwwkouiulhibvdkn
const SUPABASE_URL = "https://puqqnwwkouiulhibvdkn.supabase.co";
const SUPABASE_ANON_KEY =
  "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InB1cXFud3drb3VpdWxoaWJ2ZGtuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzA4OTg5OTIsImV4cCI6MjA4NjQ3NDk5Mn0.EXotu_PE7DYkYlBqBBhVYWXwGot_wOXDK1HihOvOxc0";

const supabase = createClient(SUPABASE_URL, SUPABASE_ANON_KEY);

// --- Types ---

interface Transaction {
  id: string;
  amount: { value: number };
  currency: string;
  description: string;
  category: string;
  timestamp: string;
  status?: string;
  userId?: string;
}

interface TransactionStats {
  totalVolume: number;
  count: number;
}

interface SpendingTrend {
  date: string;
  total: number;
}

// --- Auth Components ---

const AuthScreen = () => {
  // Default to Sign Up so you can create the Admin account immediately
  const [isLogin, setIsLogin] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  // Pre-filled credentials as requested
  const [formData, setFormData] = useState({
    email: "admin@sentinel.com",
    password: "123456Test!",
    confirmPassword: "123456Test!",
    fullName: "Admin User",
  });

  const [error, setError] = useState<string | null>(null);
  const [successMsg, setSuccessMsg] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setSuccessMsg(null);
    setIsLoading(true);

    try {
      if (isLogin) {
        const { error } = await supabase.auth.signInWithPassword({
          email: formData.email,
          password: formData.password,
        });
        if (error) throw error;
      } else {
        if (formData.password !== formData.confirmPassword) {
          throw new Error("Passwords do not match");
        }
        if (formData.password.length < 8) {
          throw new Error("Password must be at least 8 characters");
        }

        const { error } = await supabase.auth.signUp({
          email: formData.email,
          password: formData.password,
          options: {
            data: {
              full_name: formData.fullName,
            },
          },
        });
        if (error) throw error;
        setSuccessMsg("Account created! Please Sign In to continue.");
        setIsLogin(true);
      }
    } catch (err: any) {
      setError(err.message || "Authentication failed");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-[url('https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?q=80&w=2564&auto=format&fit=crop')] bg-cover bg-center flex items-center justify-center p-4">
      <div className="absolute inset-0 bg-dark-950/80 backdrop-blur-sm"></div>

      <div className="relative w-full max-w-md animate-in zoom-in-95 duration-500">
        <div className="bg-dark-900/60 backdrop-blur-xl border border-white/10 rounded-3xl shadow-2xl overflow-hidden p-8">
          <div className="flex flex-col items-center mb-8">
            <div className="w-16 h-16 bg-gradient-to-tr from-brand-500 to-violet-600 rounded-2xl flex items-center justify-center shadow-lg shadow-brand-500/30 mb-4">
              <ShieldCheck className="text-white" size={32} />
            </div>
            <h1 className="text-2xl font-bold text-white tracking-tight">
              SentinelFlow
            </h1>
            <p className="text-slate-400 text-sm">Secure Audit Environment</p>
          </div>

          <div className="flex bg-dark-950/50 rounded-xl p-1 mb-6 border border-white/5">
            <button
              onClick={() => {
                setIsLogin(true);
                setError(null);
                setSuccessMsg(null);
              }}
              className={`flex-1 py-2 rounded-lg text-sm font-medium transition-all ${isLogin ? "bg-brand-600 text-white shadow-lg" : "text-slate-400 hover:text-white"}`}
            >
              Sign In
            </button>
            <button
              onClick={() => {
                setIsLogin(false);
                setError(null);
                setSuccessMsg(null);
              }}
              className={`flex-1 py-2 rounded-lg text-sm font-medium transition-all ${!isLogin ? "bg-brand-600 text-white shadow-lg" : "text-slate-400 hover:text-white"}`}
            >
              Create Account
            </button>
          </div>

          <form onSubmit={handleSubmit} className="space-y-4">
            {(error || successMsg) && (
              <div
                className={`p-3 rounded-lg text-xs font-medium flex items-center gap-2 ${successMsg ? "bg-emerald-500/20 text-emerald-300 border border-emerald-500/30" : "bg-rose-500/20 text-rose-300 border border-rose-500/30"}`}
              >
                {successMsg ? (
                  <CheckCircle2 size={14} />
                ) : (
                  <AlertCircle size={14} />
                )}
                {successMsg || error}
              </div>
            )}

            {!isLogin && (
              <div className="space-y-4 animate-in slide-in-from-left-4">
                <div className="relative group">
                  <User
                    className="absolute left-3 top-3 text-slate-500 group-focus-within:text-brand-400 transition-colors"
                    size={18}
                  />
                  <input
                    type="text"
                    required
                    placeholder="Full Name"
                    className="w-full bg-dark-950/50 border border-white/10 rounded-xl py-2.5 pl-10 pr-4 text-white placeholder:text-slate-600 focus:outline-none focus:border-brand-500/50 focus:ring-1 focus:ring-brand-500/50 transition-all"
                    value={formData.fullName}
                    onChange={(e) =>
                      setFormData({ ...formData, fullName: e.target.value })
                    }
                  />
                </div>
              </div>
            )}

            <div className="relative group">
              <Mail
                className="absolute left-3 top-3 text-slate-500 group-focus-within:text-brand-400 transition-colors"
                size={18}
              />
              <input
                type="email"
                required
                placeholder="Email Address"
                className="w-full bg-dark-950/50 border border-white/10 rounded-xl py-2.5 pl-10 pr-4 text-white placeholder:text-slate-600 focus:outline-none focus:border-brand-500/50 focus:ring-1 focus:ring-brand-500/50 transition-all"
                value={formData.email}
                onChange={(e) =>
                  setFormData({ ...formData, email: e.target.value })
                }
              />
            </div>

            <div className="relative group">
              <Lock
                className="absolute left-3 top-3 text-slate-500 group-focus-within:text-brand-400 transition-colors"
                size={18}
              />
              <input
                type={showPassword ? "text" : "password"}
                required
                placeholder="Password"
                className="w-full bg-dark-950/50 border border-white/10 rounded-xl py-2.5 pl-10 pr-10 text-white placeholder:text-slate-600 focus:outline-none focus:border-brand-500/50 focus:ring-1 focus:ring-brand-500/50 transition-all"
                value={formData.password}
                onChange={(e) =>
                  setFormData({ ...formData, password: e.target.value })
                }
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute right-3 top-3 text-slate-500 hover:text-white transition-colors"
              >
                {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
              </button>
            </div>

            {!isLogin && (
              <div className="animate-in slide-in-from-right-4 space-y-4">
                <div className="relative group">
                  <KeyRound
                    className="absolute left-3 top-3 text-slate-500 group-focus-within:text-brand-400 transition-colors"
                    size={18}
                  />
                  <input
                    type={showPassword ? "text" : "password"}
                    required
                    placeholder="Confirm Password"
                    className="w-full bg-dark-950/50 border border-white/10 rounded-xl py-2.5 pl-10 pr-4 text-white placeholder:text-slate-600 focus:outline-none focus:border-brand-500/50 focus:ring-1 focus:ring-brand-500/50 transition-all"
                    value={formData.confirmPassword}
                    onChange={(e) =>
                      setFormData({
                        ...formData,
                        confirmPassword: e.target.value,
                      })
                    }
                  />
                </div>
              </div>
            )}

            <button
              type="submit"
              disabled={isLoading}
              className="w-full bg-brand-600 hover:bg-brand-500 text-white font-medium py-3 rounded-xl shadow-lg shadow-brand-500/20 active:scale-95 transition-all flex items-center justify-center gap-2"
            >
              {isLoading && <Activity className="animate-spin" size={18} />}
              {isLogin ? "Sign In" : "Create Account"}
              {!isLoading && <ChevronRight size={18} />}
            </button>
          </form>

          <div className="mt-6 text-center text-xs text-slate-500">
            Protected by SentinelFlow Security
          </div>
        </div>
      </div>
    </div>
  );
};

// --- Sidebar & Dashboard Components ---

const Sidebar = ({
  isOpen,
  onClose,
  activeTab,
  onTabChange,
  onLogout,
}: any) => (
  <>
    {isOpen && (
      <div
        className="fixed inset-0 bg-black/80 z-40 lg:hidden backdrop-blur-sm animate-in fade-in duration-200"
        onClick={onClose}
      />
    )}
    <aside
      className={`
      fixed lg:static inset-y-0 left-0 z-50 w-72 bg-dark-950 border-r border-slate-800 
      transform transition-transform duration-300 ease-in-out flex flex-col shadow-2xl lg:shadow-none
      ${isOpen ? "translate-x-0" : "-translate-x-full lg:translate-x-0"}
    `}
    >
      <div className="p-6 border-b border-slate-800 flex items-center gap-3">
        <div className="bg-gradient-to-tr from-brand-600 to-violet-600 p-2 rounded-lg shadow-lg shadow-brand-500/20">
          <ShieldCheck className="text-white" size={24} />
        </div>
        <div>
          <h1 className="font-bold text-lg tracking-tight text-white">
            SentinelFlow
          </h1>
          <p className="text-xs text-slate-500 font-medium">
            Audit Engine v1.0
          </p>
        </div>
        <button
          onClick={onClose}
          className="lg:hidden ml-auto text-slate-500 hover:text-white"
        >
          <X size={20} />
        </button>
      </div>
      <nav className="flex-1 p-4 space-y-2 overflow-y-auto">
        <NavItem
          icon={<LayoutDashboard size={20} />}
          label="Dashboard"
          active={activeTab === "dashboard"}
          onClick={() => {
            onTabChange("dashboard");
            onClose();
          }}
        />
        <NavItem
          icon={<Activity size={20} />}
          label="Live Monitor"
          active={activeTab === "stream"}
          onClick={() => {
            onTabChange("stream");
            onClose();
          }}
          badge="LIVE"
        />
        <NavItem
          icon={<List size={20} />}
          label="Transactions"
          active={activeTab === "list"}
          onClick={() => {
            onTabChange("list");
            onClose();
          }}
        />
        <div className="pt-6 pb-2">
          <div className="text-xs font-semibold text-slate-600 uppercase tracking-wider px-4">
            System
          </div>
        </div>
        <NavItem
          icon={<Settings size={20} />}
          label="Settings"
          active={activeTab === "settings"}
          onClick={() => {
            onTabChange("settings");
            onClose();
          }}
        />
        <NavItem
          icon={<User size={20} />}
          label="Profile"
          active={activeTab === "profile"}
          onClick={() => {
            onTabChange("profile");
            onClose();
          }}
        />
      </nav>
      <div className="p-4 border-t border-slate-800 safe-area-bottom">
        <button
          onClick={onLogout}
          className="w-full flex items-center gap-3 p-3 rounded-xl bg-slate-900/50 border border-slate-800 hover:border-rose-500/30 hover:bg-rose-500/10 transition-all cursor-pointer group text-left"
        >
          <div className="w-10 h-10 rounded-full bg-gradient-to-br from-indigo-500 to-purple-500 flex items-center justify-center text-sm font-bold text-white shadow-inner">
            U
          </div>
          <div className="flex-1 overflow-hidden">
            <p className="text-sm font-medium text-white truncate">Sign Out</p>
          </div>
          <LogOut
            size={16}
            className="text-slate-500 group-hover:text-rose-400 transition-colors"
          />
        </button>
      </div>
    </aside>
  </>
);

const NavItem = ({ icon, label, active, onClick, badge }: any) => (
  <button
    onClick={onClick}
    className={`w-full flex items-center gap-3 px-4 py-3 rounded-xl transition-all duration-200 group relative ${active ? "bg-brand-600 text-white shadow-lg shadow-brand-900/50" : "text-slate-400 hover:bg-slate-800 hover:text-white"}`}
  >
    <span
      className={
        active
          ? "text-white"
          : "text-slate-500 group-hover:text-white transition-colors"
      }
    >
      {icon}
    </span>
    <span className="font-medium">{label}</span>
    {badge && (
      <span className="ml-auto text-[10px] font-bold bg-emerald-500/20 text-emerald-400 px-2 py-0.5 rounded-full border border-emerald-500/20 animate-pulse">
        {badge}
      </span>
    )}
  </button>
);

const StatCard = ({ title, value, icon, trend }: any) => (
  <div className="bg-dark-800 border border-slate-700 rounded-xl p-5 hover:border-brand-500/30 transition-all shadow-lg group">
    <div className="flex justify-between items-start mb-4">
      <div className="p-2 bg-brand-500/10 rounded-lg text-brand-500 group-hover:bg-brand-500/20 transition-colors">
        {icon}
      </div>
      {trend && (
        <div className="flex items-center text-xs font-medium px-2 py-1 rounded-full bg-emerald-500/10 text-emerald-400 border border-emerald-500/10">
          <ArrowUpRight size={12} className="mr-1" />
          {trend}
        </div>
      )}
    </div>
    <h3 className="text-slate-400 text-sm font-medium mb-1">{title}</h3>
    <div className="text-2xl font-bold text-white tracking-tight">{value}</div>
  </div>
);

const SpendingChart = ({ data }: { data: SpendingTrend[] }) => {
  if (!data || data.length === 0)
    return (
      <div className="h-64 flex items-center justify-center text-slate-500 bg-dark-800 rounded-xl border border-slate-700">
        No trend data available
      </div>
    );

  return (
    <div className="bg-dark-800 border border-slate-700 rounded-xl p-6 shadow-lg">
      <h3 className="text-white font-semibold mb-6 flex items-center gap-2">
        <Activity size={18} className="text-brand-500" /> Spending Trends (Last
        30 Days)
      </h3>
      <div className="h-64 w-full">
        <ResponsiveContainer width="100%" height="100%">
          <AreaChart data={data}>
            <defs>
              <linearGradient id="colorTotal" x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%" stopColor="#0ea5e9" stopOpacity={0.3} />
                <stop offset="95%" stopColor="#0ea5e9" stopOpacity={0} />
              </linearGradient>
            </defs>
            <CartesianGrid
              strokeDasharray="3 3"
              stroke="#334155"
              vertical={false}
            />
            <XAxis
              dataKey="date"
              stroke="#94a3b8"
              fontSize={12}
              tickLine={false}
              axisLine={false}
              tickFormatter={(value) =>
                new Date(value).toLocaleDateString(undefined, {
                  month: "short",
                  day: "numeric",
                })
              }
              minTickGap={30}
            />
            <YAxis
              stroke="#94a3b8"
              fontSize={12}
              tickLine={false}
              axisLine={false}
              tickFormatter={(value) => `$${value}`}
            />
            <Tooltip
              contentStyle={{
                backgroundColor: "#1e293b",
                borderColor: "#334155",
                color: "#f8fafc",
                borderRadius: "0.5rem",
              }}
              itemStyle={{ color: "#0ea5e9" }}
              formatter={(value: number) => [
                `$${value.toFixed(2)}`,
                "Total Spent",
              ]}
              labelFormatter={(label) =>
                new Date(label).toLocaleDateString(undefined, {
                  weekday: "long",
                  year: "numeric",
                  month: "long",
                  day: "numeric",
                })
              }
            />
            <Area
              type="monotone"
              dataKey="total"
              stroke="#0ea5e9"
              strokeWidth={3}
              fillOpacity={1}
              fill="url(#colorTotal)"
            />
          </AreaChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

const ProfileView = ({ token, user }: { token: string; user: any }) => {
  const [profile, setProfile] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [isEditing, setIsEditing] = useState(false);
  const [editForm, setEditForm] = useState({ fullName: "" });
  const [msg, setMsg] = useState({ type: "", text: "" });

  const fetchProfile = async () => {
    try {
      const res = await fetch("/api/users/me", {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (res.ok) {
        const data = await res.json();
        setProfile(data);
        setEditForm({ fullName: data.fullName });
      }
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProfile();
  }, [token]);

  const handleUpdate = async (e: React.FormEvent) => {
    e.preventDefault();
    setMsg({ type: "", text: "" });
    try {
      const res = await fetch("/api/users/me", {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ fullName: editForm.fullName }),
      });
      if (!res.ok) throw new Error("Update failed");
      const updated = await res.json();
      setProfile(updated);
      setIsEditing(false);
      setMsg({ type: "success", text: "Profile updated successfully" });
    } catch (e) {
      setMsg({ type: "error", text: "Failed to update profile" });
    }
  };

  if (loading)
    return (
      <div className="text-center mt-20 text-slate-500">Loading profile...</div>
    );

  return (
    <div className="max-w-2xl mx-auto mt-10 p-6 bg-dark-800 rounded-2xl border border-slate-700 shadow-xl">
      <div className="flex items-center gap-4 mb-8">
        <div className="w-16 h-16 rounded-full bg-gradient-to-br from-brand-500 to-violet-600 flex items-center justify-center text-2xl font-bold text-white shadow-lg">
          {profile?.fullName?.charAt(0) || user?.email?.charAt(0) || "U"}
        </div>
        <div>
          <h2 className="text-2xl font-bold text-white">
            {profile?.fullName || "User"}
          </h2>
          <p className="text-slate-400">{profile?.email || user?.email}</p>
        </div>
        <div className="ml-auto">
          <span
            className={`px-3 py-1 rounded-full text-xs font-bold border ${profile?.status === "ACTIVE" ? "bg-emerald-500/10 text-emerald-400 border-emerald-500/20" : "bg-slate-700 text-slate-300 border-slate-600"}`}
          >
            {profile?.status || "UNKNOWN"}
          </span>
        </div>
      </div>

      {msg.text && (
        <div
          className={`p-3 rounded-lg text-sm mb-4 flex items-center gap-2 ${msg.type === "success" ? "bg-emerald-500/20 text-emerald-300" : "bg-rose-500/20 text-rose-300"}`}
        >
          {msg.type === "success" ? (
            <CheckCircle2 size={16} />
          ) : (
            <AlertCircle size={16} />
          )}{" "}
          {msg.text}
        </div>
      )}

      {isEditing ? (
        <form
          onSubmit={handleUpdate}
          className="space-y-4 animate-in fade-in slide-in-from-top-2"
        >
          <div>
            <label className="block text-xs font-bold text-slate-500 uppercase mb-1">
              Full Name
            </label>
            <input
              type="text"
              className="w-full bg-dark-950 border border-slate-600 rounded-xl p-3 text-white focus:border-brand-500 focus:ring-1 focus:ring-brand-500 outline-none transition-all"
              value={editForm.fullName}
              onChange={(e) =>
                setEditForm({ ...editForm, fullName: e.target.value })
              }
              required
            />
          </div>
          <div className="flex gap-3 pt-2">
            <button
              type="button"
              onClick={() => setIsEditing(false)}
              className="flex-1 py-2.5 rounded-xl border border-slate-600 text-slate-300 hover:bg-slate-800 transition-colors"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="flex-1 py-2.5 rounded-xl bg-brand-600 text-white font-medium hover:bg-brand-500 shadow-lg shadow-brand-500/20 transition-all"
            >
              Save Changes
            </button>
          </div>
        </form>
      ) : (
        <div className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="p-4 bg-dark-950/50 rounded-xl border border-slate-800">
              <label className="block text-[10px] font-bold text-slate-500 uppercase mb-1">
                Role
              </label>
              <div className="text-slate-200 font-medium flex items-center gap-2">
                <ShieldCheck size={14} className="text-brand-500" />{" "}
                {profile?.role}
              </div>
            </div>
            <div className="p-4 bg-dark-950/50 rounded-xl border border-slate-800">
              <label className="block text-[10px] font-bold text-slate-500 uppercase mb-1">
                Member Since
              </label>
              <div className="text-slate-200 font-medium flex items-center gap-2">
                <Calendar size={14} className="text-slate-500" />{" "}
                {new Date(profile?.joinedAt).toLocaleDateString()}
              </div>
            </div>
            <div className="p-4 bg-dark-950/50 rounded-xl border border-slate-800 col-span-1 md:col-span-2">
              <label className="block text-[10px] font-bold text-slate-500 uppercase mb-1">
                Username / ID
              </label>
              <div className="text-slate-400 font-mono text-xs">
                {profile?.id}
              </div>
            </div>
          </div>

          <button
            onClick={() => setIsEditing(true)}
            className="w-full mt-6 py-3 rounded-xl border border-brand-500/30 text-brand-400 hover:bg-brand-500/10 font-medium transition-all flex items-center justify-center gap-2"
          >
            Edit Profile
          </button>
        </div>
      )}
    </div>
  );
};

const TransactionFormModal = ({
  isOpen,
  onClose,
  onSuccess,
  token,
  userId,
}: any) => {
  if (!isOpen) return null;
  const [formData, setFormData] = useState({
    amount: "",
    currency: "USD",
    description: "",
    category: "General",
    userId: userId || "unknown",
    timestamp: new Date().toISOString().slice(0, 16),
  });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    try {
      const res = await fetch("/api/transactions", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          ...formData,
          amount: parseFloat(formData.amount),
          timestamp: new Date(formData.timestamp).toISOString(),
        }),
      });
      if (!res.ok) throw new Error("Failed");
      onSuccess();
      onClose();
    } catch (e) {
      setError("Failed to submit");
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black/80 backdrop-blur-sm z-[60] flex items-center justify-center p-4">
      <div className="bg-dark-900 border border-slate-700 rounded-2xl w-full max-w-lg p-6">
        <div className="flex justify-between mb-4">
          <h2 className="text-white font-bold">New Transaction</h2>
          <button onClick={onClose}>
            <X className="text-slate-400" />
          </button>
        </div>
        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            type="number"
            required
            className="w-full bg-dark-800 border border-slate-700 rounded-lg p-2 text-white"
            placeholder="Amount"
            value={formData.amount}
            onChange={(e) =>
              setFormData({ ...formData, amount: e.target.value })
            }
          />
          <input
            type="text"
            required
            className="w-full bg-dark-800 border border-slate-700 rounded-lg p-2 text-white"
            placeholder="Description"
            value={formData.description}
            onChange={(e) =>
              setFormData({ ...formData, description: e.target.value })
            }
          />
          <button
            type="submit"
            disabled={isSubmitting}
            className="w-full bg-brand-600 text-white p-2 rounded-lg"
          >
            {isSubmitting ? "..." : "Submit"}
          </button>
        </form>
      </div>
    </div>
  );
};

const App = () => {
  const [session, setSession] = useState<any>(null);
  const [isSidebarOpen, setSidebarOpen] = useState(false);
  const [activeTab, setActiveTab] = useState("dashboard");
  const [liveTransactions, setLiveTransactions] = useState<Transaction[]>([]);
  const [historyTransactions, setHistoryTransactions] = useState<Transaction[]>(
    [],
  );
  const [spendingTrends, setSpendingTrends] = useState<SpendingTrend[]>([]);
  const [isModalOpen, setModalOpen] = useState(false);
  const [filters, setFilters] = useState({
    startDate: "",
    endDate: "",
    category: "",
    userId: "",
  });
  const [isLoadingHistory, setIsLoadingHistory] = useState(false);

  useEffect(() => {
    supabase.auth
      .getSession()
      .then(({ data: { session } }) => setSession(session));
    const {
      data: { subscription },
    } = supabase.auth.onAuthStateChange((_event, session) =>
      setSession(session),
    );
    return () => subscription.unsubscribe();
  }, []);

  const token = session?.access_token;
  const user = session?.user;

  useEffect(() => {
    if (!token) return;
    const es = new EventSource("/api/transactions/stream");
    es.onmessage = (e) => {
      try {
        setLiveTransactions((prev) =>
          [JSON.parse(e.data), ...prev].slice(0, 50),
        );
      } catch (err) {}
    };
    return () => es.close();
  }, [token]);

  // Fetch Trends
  useEffect(() => {
    if (!token || activeTab !== "dashboard") return;
    const fetchTrends = async () => {
      try {
        const res = await fetch("/api/transactions/trends", {
          headers: { Authorization: `Bearer ${token}` },
        });
        if (res.ok) {
          const data = await res.json();
          setSpendingTrends(data);
        }
      } catch (e) {
        console.error("Error fetching trends", e);
      }
    };
    fetchTrends();
  }, [token, activeTab]);

  useEffect(() => {
    if (!token || activeTab !== "list") return;
    const fetchHistory = async () => {
      setIsLoadingHistory(true);
      try {
        const params = new URLSearchParams();
        if (filters.startDate)
          params.append("startDate", new Date(filters.startDate).toISOString());
        if (filters.endDate)
          params.append("endDate", new Date(filters.endDate).toISOString());
        if (filters.category) params.append("category", filters.category);
        if (filters.userId) params.append("userId", filters.userId);

        const res = await fetch(`/api/transactions?${params.toString()}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        if (res.ok) {
          const data = await res.json();
          setHistoryTransactions(data);
        }
      } catch (e) {
        console.error(e);
      } finally {
        setIsLoadingHistory(false);
      }
    };
    const timeout = setTimeout(fetchHistory, 500);
    return () => clearTimeout(timeout);
  }, [token, activeTab, filters]);

  if (!session) return <AuthScreen />;

  const handleLogout = async () => await supabase.auth.signOut();

  const stats = {
    totalVolume: liveTransactions.reduce((acc, t) => acc + t.amount.value, 0),
    count: liveTransactions.length,
  };

  const renderTransactionList = (
    list: Transaction[],
    loading: boolean = false,
  ) => {
    if (loading)
      return (
        <div className="text-center py-12 text-slate-500">
          <Activity className="animate-spin mx-auto mb-2" />
          Loading data...
        </div>
      );
    if (list.length === 0)
      return (
        <div className="text-center py-12 text-slate-500">
          No transactions found.
        </div>
      );
    return list.map((t) => (
      <div
        key={t.id}
        className="bg-dark-800 p-4 rounded-xl border border-slate-700 flex justify-between mb-2"
      >
        <div>
          <div className="text-white font-medium">{t.description}</div>
          <div className="flex gap-2 text-xs text-slate-500 mt-1">
            <span className="bg-slate-700 px-1.5 rounded">
              {t.category || "N/A"}
            </span>
            <span>{new Date(t.timestamp).toLocaleString()}</span>
          </div>
        </div>
        <div className="text-emerald-400 font-mono font-bold">
          ${t.amount.value}
        </div>
      </div>
    ));
  };

  return (
    <div className="flex h-screen bg-dark-900 text-slate-100 font-sans overflow-hidden safe-area-top">
      <Sidebar
        isOpen={isSidebarOpen}
        onClose={() => setSidebarOpen(false)}
        activeTab={activeTab}
        onTabChange={setActiveTab}
        onLogout={handleLogout}
      />
      <div className="flex-1 flex flex-col min-w-0 relative">
        <header className="h-16 border-b border-slate-800 bg-dark-900/90 backdrop-blur flex items-center justify-between px-4 sticky top-0 z-20">
          <div className="flex items-center gap-4">
            <button
              onClick={() => setSidebarOpen(true)}
              className="lg:hidden text-slate-400"
            >
              <Menu size={24} />
            </button>
            <h2 className="text-white font-semibold capitalize">
              {activeTab === "stream" ? "Live Monitor" : activeTab}
            </h2>
          </div>
          <button
            onClick={() => setModalOpen(true)}
            className="bg-brand-600 text-white px-4 py-2 rounded-lg text-sm font-medium flex items-center gap-2"
          >
            <Plus size={16} /> New
          </button>
        </header>

        <main className="flex-1 overflow-auto p-4 lg:p-8 custom-scrollbar">
          {activeTab === "dashboard" && (
            <div className="space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <StatCard
                  title="Volume (Live)"
                  value={`$${stats.totalVolume.toFixed(2)}`}
                  icon={<Wallet size={20} />}
                />
                <StatCard
                  title="Transactions (Live)"
                  value={stats.count}
                  icon={<Activity size={20} />}
                />
              </div>

              <SpendingChart data={spendingTrends} />

              <h3 className="text-slate-400 font-semibold mt-8 mb-4">
                Recent Activity (Live Stream)
              </h3>
              <div className="space-y-2">
                {renderTransactionList(liveTransactions)}
              </div>
            </div>
          )}

          {activeTab === "stream" && (
            <div className="space-y-4">
              <div className="flex items-center gap-2 text-emerald-400 text-sm font-medium animate-pulse mb-4">
                <span className="w-2 h-2 rounded-full bg-emerald-500"></span>{" "}
                Live Socket Connected
              </div>
              {renderTransactionList(liveTransactions)}
            </div>
          )}

          {activeTab === "list" && (
            <div className="space-y-6">
              <div className="bg-dark-800 border border-slate-700 rounded-xl p-4">
                <div className="flex items-center gap-2 mb-4 text-sm font-semibold text-slate-300">
                  <Filter size={16} /> Filters
                </div>
                <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
                  <div>
                    <label className="text-[10px] text-slate-500 uppercase font-bold mb-1 block">
                      Start Date
                    </label>
                    <input
                      type="datetime-local"
                      className="w-full bg-dark-900 border border-slate-600 rounded-lg py-2 px-2 text-sm text-white outline-none [color-scheme:dark]"
                      value={filters.startDate}
                      onChange={(e) =>
                        setFilters({ ...filters, startDate: e.target.value })
                      }
                    />
                  </div>
                  <div>
                    <label className="text-[10px] text-slate-500 uppercase font-bold mb-1 block">
                      End Date
                    </label>
                    <input
                      type="datetime-local"
                      className="w-full bg-dark-900 border border-slate-600 rounded-lg py-2 px-2 text-sm text-white outline-none [color-scheme:dark]"
                      value={filters.endDate}
                      onChange={(e) =>
                        setFilters({ ...filters, endDate: e.target.value })
                      }
                    />
                  </div>
                  <div>
                    <label className="text-[10px] text-slate-500 uppercase font-bold mb-1 block">
                      Category
                    </label>
                    <select
                      className="w-full bg-dark-900 border border-slate-600 rounded-lg py-2 px-3 text-sm text-white outline-none"
                      value={filters.category}
                      onChange={(e) =>
                        setFilters({ ...filters, category: e.target.value })
                      }
                    >
                      <option value="">All</option>
                      <option>General</option>
                    </select>
                  </div>
                </div>
              </div>
              <div>
                {renderTransactionList(historyTransactions, isLoadingHistory)}
              </div>
            </div>
          )}

          {activeTab === "profile" && <ProfileView token={token} user={user} />}
        </main>
      </div>
      <TransactionFormModal
        isOpen={isModalOpen}
        onClose={() => setModalOpen(false)}
        token={token}
        userId={user?.id}
        onSuccess={() => {}}
      />
    </div>
  );
};

const root = createRoot(document.getElementById("root")!);
root.render(<App />);
