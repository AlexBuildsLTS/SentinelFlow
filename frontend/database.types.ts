export type Json =
  | string
  | number
  | boolean
  | null
  | { [key: string]: Json | undefined }
  | Json[]

export type Database = {
  // Allows to automatically instantiate createClient with right options
  // instead of createClient<Database, { PostgrestVersion: 'XX' }>(URL, KEY)
  __InternalSupabase: {
    PostgrestVersion: "14.1"
  }
  public: {
    Tables: {
      ai_chat_history: {
        Row: {
          created_at: string | null
          id: string
          is_thinking: boolean | null
          prompt: string
          response: string | null
          user_id: string | null
        }
        Insert: {
          created_at?: string | null
          id?: string
          is_thinking?: boolean | null
          prompt: string
          response?: string | null
          user_id?: string | null
        }
        Update: {
          created_at?: string | null
          id?: string
          is_thinking?: boolean | null
          prompt?: string
          response?: string | null
          user_id?: string | null
        }
        Relationships: [
          {
            foreignKeyName: "ai_chat_history_user_id_fkey"
            columns: ["user_id"]
            isOneToOne: false
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
        ]
      }
      ai_insights: {
        Row: {
          content: string
          context_type: string
          created_at: string | null
          id: string
          is_dismissed: boolean | null
          related_transaction_id: string | null
          user_id: string
        }
        Insert: {
          content: string
          context_type: string
          created_at?: string | null
          id?: string
          is_dismissed?: boolean | null
          related_transaction_id?: string | null
          user_id: string
        }
        Update: {
          content?: string
          context_type?: string
          created_at?: string | null
          id?: string
          is_dismissed?: boolean | null
          related_transaction_id?: string | null
          user_id?: string
        }
        Relationships: [
          {
            foreignKeyName: "ai_insights_related_transaction_id_fkey"
            columns: ["related_transaction_id"]
            isOneToOne: false
            referencedRelation: "transactions"
            referencedColumns: ["id"]
          },
          {
            foreignKeyName: "ai_insights_user_id_fkey"
            columns: ["user_id"]
            isOneToOne: false
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
        ]
      }
      audit_logs: {
        Row: {
          action: Database["public"]["Enums"]["audit_action"]
          actor_id: string | null
          entity_id: string
          entity_name: string
          id: string
          ip_address: unknown
          new_values: Json | null
          old_values: Json | null
          timestamp: string | null
          user_agent: string | null
        }
        Insert: {
          action: Database["public"]["Enums"]["audit_action"]
          actor_id?: string | null
          entity_id: string
          entity_name: string
          id?: string
          ip_address?: unknown
          new_values?: Json | null
          old_values?: Json | null
          timestamp?: string | null
          user_agent?: string | null
        }
        Update: {
          action?: Database["public"]["Enums"]["audit_action"]
          actor_id?: string | null
          entity_id?: string
          entity_name?: string
          id?: string
          ip_address?: unknown
          new_values?: Json | null
          old_values?: Json | null
          timestamp?: string | null
          user_agent?: string | null
        }
        Relationships: [
          {
            foreignKeyName: "audit_logs_actor_id_fkey"
            columns: ["actor_id"]
            isOneToOne: false
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
        ]
      }
      auth_events: {
        Row: {
          created_at: string | null
          event_type: string
          id: string
          user_id: string | null
        }
        Insert: {
          created_at?: string | null
          event_type: string
          id?: string
          user_id?: string | null
        }
        Update: {
          created_at?: string | null
          event_type?: string
          id?: string
          user_id?: string | null
        }
        Relationships: [
          {
            foreignKeyName: "auth_events_user_id_fkey"
            columns: ["user_id"]
            isOneToOne: false
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
        ]
      }
      biometric_keys: {
        Row: {
          credential_id: string
          device_name: string | null
          id: string
          last_used: string | null
          public_key: string
          user_id: string
        }
        Insert: {
          credential_id: string
          device_name?: string | null
          id?: string
          last_used?: string | null
          public_key: string
          user_id: string
        }
        Update: {
          credential_id?: string
          device_name?: string | null
          id?: string
          last_used?: string | null
          public_key?: string
          user_id?: string
        }
        Relationships: [
          {
            foreignKeyName: "biometric_keys_user_id_fkey"
            columns: ["user_id"]
            isOneToOne: false
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
        ]
      }
      budgets: {
        Row: {
          alert_threshold_percent: number | null
          amount_limit: number
          category_id: string | null
          created_at: string | null
          current_spend: number | null
          end_date: string | null
          id: string
          name: string | null
          period: string | null
          start_date: string
          updated_at: string | null
          user_id: string
        }
        Insert: {
          alert_threshold_percent?: number | null
          amount_limit: number
          category_id?: string | null
          created_at?: string | null
          current_spend?: number | null
          end_date?: string | null
          id?: string
          name?: string | null
          period?: string | null
          start_date: string
          updated_at?: string | null
          user_id: string
        }
        Update: {
          alert_threshold_percent?: number | null
          amount_limit?: number
          category_id?: string | null
          created_at?: string | null
          current_spend?: number | null
          end_date?: string | null
          id?: string
          name?: string | null
          period?: string | null
          start_date?: string
          updated_at?: string | null
          user_id?: string
        }
        Relationships: [
          {
            foreignKeyName: "budgets_category_id_fkey"
            columns: ["category_id"]
            isOneToOne: false
            referencedRelation: "categories"
            referencedColumns: ["id"]
          },
          {
            foreignKeyName: "budgets_user_id_fkey"
            columns: ["user_id"]
            isOneToOne: false
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
        ]
      }
      categories: {
        Row: {
          color_hex: string | null
          created_at: string | null
          icon_slug: string | null
          id: string
          is_archived: boolean | null
          name: string
          parent_category_id: string | null
          type: Database["public"]["Enums"]["transaction_type"]
          user_id: string | null
        }
        Insert: {
          color_hex?: string | null
          created_at?: string | null
          icon_slug?: string | null
          id?: string
          is_archived?: boolean | null
          name: string
          parent_category_id?: string | null
          type?: Database["public"]["Enums"]["transaction_type"]
          user_id?: string | null
        }
        Update: {
          color_hex?: string | null
          created_at?: string | null
          icon_slug?: string | null
          id?: string
          is_archived?: boolean | null
          name?: string
          parent_category_id?: string | null
          type?: Database["public"]["Enums"]["transaction_type"]
          user_id?: string | null
        }
        Relationships: [
          {
            foreignKeyName: "categories_parent_category_id_fkey"
            columns: ["parent_category_id"]
            isOneToOne: false
            referencedRelation: "categories"
            referencedColumns: ["id"]
          },
          {
            foreignKeyName: "categories_user_id_fkey"
            columns: ["user_id"]
            isOneToOne: false
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
        ]
      }
      chat_messages: {
        Row: {
          attachment_url: string | null
          content: string
          created_at: string | null
          encryption_iv: string | null
          id: string
          is_encrypted: boolean | null
          key_tag: string | null
          receiver_id: string
          reply_to_id: string | null
          sender_id: string
        }
        Insert: {
          attachment_url?: string | null
          content: string
          created_at?: string | null
          encryption_iv?: string | null
          id?: string
          is_encrypted?: boolean | null
          key_tag?: string | null
          receiver_id: string
          reply_to_id?: string | null
          sender_id: string
        }
        Update: {
          attachment_url?: string | null
          content?: string
          created_at?: string | null
          encryption_iv?: string | null
          id?: string
          is_encrypted?: boolean | null
          key_tag?: string | null
          receiver_id?: string
          reply_to_id?: string | null
          sender_id?: string
        }
        Relationships: [
          {
            foreignKeyName: "chat_messages_receiver_id_fkey"
            columns: ["receiver_id"]
            isOneToOne: false
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
          {
            foreignKeyName: "chat_messages_reply_to_id_fkey"
            columns: ["reply_to_id"]
            isOneToOne: false
            referencedRelation: "chat_messages"
            referencedColumns: ["id"]
          },
          {
            foreignKeyName: "chat_messages_sender_id_fkey"
            columns: ["sender_id"]
            isOneToOne: false
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
        ]
      }
      config_audits: {
        Row: {
          created_at: string | null
          id: string
          setting_key: string
          status: string | null
          user_id: string | null
        }
        Insert: {
          created_at?: string | null
          id?: string
          setting_key: string
          status?: string | null
          user_id?: string | null
        }
        Update: {
          created_at?: string | null
          id?: string
          setting_key?: string
          status?: string | null
          user_id?: string | null
        }
        Relationships: [
          {
            foreignKeyName: "config_audits_user_id_fkey"
            columns: ["user_id"]
            isOneToOne: false
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
        ]
      }
      notifications: {
        Row: {
          action_link: string | null
          created_at: string | null
          id: string
          is_read: boolean | null
          message: string
          title: string
          type: string
          user_id: string
        }
        Insert: {
          action_link?: string | null
          created_at?: string | null
          id?: string
          is_read?: boolean | null
          message: string
          title: string
          type: string
          user_id: string
        }
        Update: {
          action_link?: string | null
          created_at?: string | null
          id?: string
          is_read?: boolean | null
          message?: string
          title?: string
          type?: string
          user_id?: string
        }
        Relationships: [
          {
            foreignKeyName: "notifications_user_id_fkey"
            columns: ["user_id"]
            isOneToOne: false
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
        ]
      }
      password_reset_logs: {
        Row: {
          id: string
          ip_address: unknown
          requested_at: string | null
          user_id: string | null
        }
        Insert: {
          id?: string
          ip_address?: unknown
          requested_at?: string | null
          user_id?: string | null
        }
        Update: {
          id?: string
          ip_address?: unknown
          requested_at?: string | null
          user_id?: string | null
        }
        Relationships: [
          {
            foreignKeyName: "password_reset_logs_user_id_fkey"
            columns: ["user_id"]
            isOneToOne: false
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
        ]
      }
      private_messages: {
        Row: {
          encrypted_content: string
          id: string
          is_read: boolean | null
          receiver_id: string
          sender_id: string
          sent_at: string | null
        }
        Insert: {
          encrypted_content: string
          id?: string
          is_read?: boolean | null
          receiver_id: string
          sender_id: string
          sent_at?: string | null
        }
        Update: {
          encrypted_content?: string
          id?: string
          is_read?: boolean | null
          receiver_id?: string
          sender_id?: string
          sent_at?: string | null
        }
        Relationships: [
          {
            foreignKeyName: "private_messages_receiver_id_fkey"
            columns: ["receiver_id"]
            isOneToOne: false
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
          {
            foreignKeyName: "private_messages_sender_id_fkey"
            columns: ["sender_id"]
            isOneToOne: false
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
        ]
      }
      refresh_tokens: {
        Row: {
          created_at: string | null
          device_info: string | null
          expiry_date: string
          id: string
          ip_address: unknown
          token_hash: string
          user_id: string
        }
        Insert: {
          created_at?: string | null
          device_info?: string | null
          expiry_date: string
          id?: string
          ip_address?: unknown
          token_hash: string
          user_id: string
        }
        Update: {
          created_at?: string | null
          device_info?: string | null
          expiry_date?: string
          id?: string
          ip_address?: unknown
          token_hash?: string
          user_id?: string
        }
        Relationships: [
          {
            foreignKeyName: "refresh_tokens_user_id_fkey"
            columns: ["user_id"]
            isOneToOne: false
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
        ]
      }
      subscriptions: {
        Row: {
          id: string
          next_billing_date: string | null
          start_date: string | null
          status: string
          stripe_customer_id: string | null
          tier: string
          updated_at: string | null
          user_id: string
        }
        Insert: {
          id?: string
          next_billing_date?: string | null
          start_date?: string | null
          status?: string
          stripe_customer_id?: string | null
          tier?: string
          updated_at?: string | null
          user_id: string
        }
        Update: {
          id?: string
          next_billing_date?: string | null
          start_date?: string | null
          status?: string
          stripe_customer_id?: string | null
          tier?: string
          updated_at?: string | null
          user_id?: string
        }
        Relationships: [
          {
            foreignKeyName: "subscriptions_user_id_fkey"
            columns: ["user_id"]
            isOneToOne: false
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
        ]
      }
      support_tickets: {
        Row: {
          created_at: string | null
          description: string
          id: string
          priority: string | null
          status: string | null
          subject: string
          user_id: string
        }
        Insert: {
          created_at?: string | null
          description: string
          id?: string
          priority?: string | null
          status?: string | null
          subject: string
          user_id: string
        }
        Update: {
          created_at?: string | null
          description?: string
          id?: string
          priority?: string | null
          status?: string | null
          subject?: string
          user_id?: string
        }
        Relationships: [
          {
            foreignKeyName: "support_tickets_user_id_fkey"
            columns: ["user_id"]
            isOneToOne: false
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
        ]
      }
      transactions: {
        Row: {
          ai_category_prediction: string | null
          amount: number
          category_id: string | null
          created_at: string | null
          currency: string
          deleted_at: string | null
          description: string | null
          exchange_rate: number | null
          id: string
          is_recurring: boolean | null
          location: string | null
          merchant_name: string | null
          notes: string | null
          processed_at: string | null
          receipt_url: string | null
          recurring_frequency: string | null
          risk_score: number | null
          status: Database["public"]["Enums"]["transaction_status"]
          transaction_date: string
          type: Database["public"]["Enums"]["transaction_type"]
          updated_at: string | null
          user_id: string
        }
        Insert: {
          ai_category_prediction?: string | null
          amount: number
          category_id?: string | null
          created_at?: string | null
          currency?: string
          deleted_at?: string | null
          description?: string | null
          exchange_rate?: number | null
          id?: string
          is_recurring?: boolean | null
          location?: string | null
          merchant_name?: string | null
          notes?: string | null
          processed_at?: string | null
          receipt_url?: string | null
          recurring_frequency?: string | null
          risk_score?: number | null
          status?: Database["public"]["Enums"]["transaction_status"]
          transaction_date: string
          type?: Database["public"]["Enums"]["transaction_type"]
          updated_at?: string | null
          user_id: string
        }
        Update: {
          ai_category_prediction?: string | null
          amount?: number
          category_id?: string | null
          created_at?: string | null
          currency?: string
          deleted_at?: string | null
          description?: string | null
          exchange_rate?: number | null
          id?: string
          is_recurring?: boolean | null
          location?: string | null
          merchant_name?: string | null
          notes?: string | null
          processed_at?: string | null
          receipt_url?: string | null
          recurring_frequency?: string | null
          risk_score?: number | null
          status?: Database["public"]["Enums"]["transaction_status"]
          transaction_date?: string
          type?: Database["public"]["Enums"]["transaction_type"]
          updated_at?: string | null
          user_id?: string
        }
        Relationships: [
          {
            foreignKeyName: "transactions_category_id_fkey"
            columns: ["category_id"]
            isOneToOne: false
            referencedRelation: "categories"
            referencedColumns: ["id"]
          },
          {
            foreignKeyName: "transactions_user_id_fkey"
            columns: ["user_id"]
            isOneToOne: false
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
        ]
      }
      users: {
        Row: {
          ai_provider: string | null
          avatar_url: string | null
          biometric_enabled: boolean | null
          created_at: string | null
          deleted_at: string | null
          email: string
          encrypted_ai_key: string | null
          failed_login_attempts: number | null
          full_name: string | null
          id: string
          last_active_at: string | null
          last_login_at: string | null
          local_ai_key_hash: string | null
          password_hash: string
          phone_number: string | null
          recovery_email: string | null
          requires_2fa: boolean | null
          role: Database["public"]["Enums"]["user_role"]
          settings: Json | null
          status: Database["public"]["Enums"]["account_status"]
          two_factor_enabled: boolean | null
          updated_at: string | null
          username: string
        }
        Insert: {
          ai_provider?: string | null
          avatar_url?: string | null
          biometric_enabled?: boolean | null
          created_at?: string | null
          deleted_at?: string | null
          email: string
          encrypted_ai_key?: string | null
          failed_login_attempts?: number | null
          full_name?: string | null
          id?: string
          last_active_at?: string | null
          last_login_at?: string | null
          local_ai_key_hash?: string | null
          password_hash: string
          phone_number?: string | null
          recovery_email?: string | null
          requires_2fa?: boolean | null
          role?: Database["public"]["Enums"]["user_role"]
          settings?: Json | null
          status?: Database["public"]["Enums"]["account_status"]
          two_factor_enabled?: boolean | null
          updated_at?: string | null
          username: string
        }
        Update: {
          ai_provider?: string | null
          avatar_url?: string | null
          biometric_enabled?: boolean | null
          created_at?: string | null
          deleted_at?: string | null
          email?: string
          encrypted_ai_key?: string | null
          failed_login_attempts?: number | null
          full_name?: string | null
          id?: string
          last_active_at?: string | null
          last_login_at?: string | null
          local_ai_key_hash?: string | null
          password_hash?: string
          phone_number?: string | null
          recovery_email?: string | null
          requires_2fa?: boolean | null
          role?: Database["public"]["Enums"]["user_role"]
          settings?: Json | null
          status?: Database["public"]["Enums"]["account_status"]
          two_factor_enabled?: boolean | null
          updated_at?: string | null
          username?: string
        }
        Relationships: []
      }
    }
    Views: {
      transaction_trends: {
        Row: {
          amount: number | null
          category_id: string | null
          transaction_date: string | null
          user_id: string | null
        }
        Insert: {
          amount?: number | null
          category_id?: string | null
          transaction_date?: string | null
          user_id?: string | null
        }
        Update: {
          amount?: number | null
          category_id?: string | null
          transaction_date?: string | null
          user_id?: string | null
        }
        Relationships: [
          {
            foreignKeyName: "transactions_category_id_fkey"
            columns: ["category_id"]
            isOneToOne: false
            referencedRelation: "categories"
            referencedColumns: ["id"]
          },
          {
            foreignKeyName: "transactions_user_id_fkey"
            columns: ["user_id"]
            isOneToOne: false
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
        ]
      }
    }
    Functions: {
      [_ in never]: never
    }
    Enums: {
      account_status: "ACTIVE" | "SUSPENDED" | "BANNED" | "PENDING_VERIFICATION"
      audit_action:
        | "CREATE"
        | "UPDATE"
        | "DELETE"
        | "LOGIN"
        | "LOGOUT"
        | "EXPORT_DATA"
      transaction_status:
        | "PENDING"
        | "COMPLETED"
        | "FAILED"
        | "CANCELLED"
        | "REFUNDED"
      transaction_type: "INCOME" | "EXPENSE" | "TRANSFER"
      user_role: "MEMBER" | "PREMIUM" | "MODERATOR" | "ADMIN"
    }
    CompositeTypes: {
      [_ in never]: never
    }
  }
}

type DatabaseWithoutInternals = Omit<Database, "__InternalSupabase">

type DefaultSchema = DatabaseWithoutInternals[Extract<keyof Database, "public">]

export type Tables<
  DefaultSchemaTableNameOrOptions extends
    | keyof (DefaultSchema["Tables"] & DefaultSchema["Views"])
    | { schema: keyof DatabaseWithoutInternals },
  TableName extends DefaultSchemaTableNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals
  }
    ? keyof (DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"] &
        DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Views"])
    : never = never,
> = DefaultSchemaTableNameOrOptions extends {
  schema: keyof DatabaseWithoutInternals
}
  ? (DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"] &
      DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Views"])[TableName] extends {
      Row: infer R
    }
    ? R
    : never
  : DefaultSchemaTableNameOrOptions extends keyof (DefaultSchema["Tables"] &
        DefaultSchema["Views"])
    ? (DefaultSchema["Tables"] &
        DefaultSchema["Views"])[DefaultSchemaTableNameOrOptions] extends {
        Row: infer R
      }
      ? R
      : never
    : never

export type TablesInsert<
  DefaultSchemaTableNameOrOptions extends
    | keyof DefaultSchema["Tables"]
    | { schema: keyof DatabaseWithoutInternals },
  TableName extends DefaultSchemaTableNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals
  }
    ? keyof DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"]
    : never = never,
> = DefaultSchemaTableNameOrOptions extends {
  schema: keyof DatabaseWithoutInternals
}
  ? DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"][TableName] extends {
      Insert: infer I
    }
    ? I
    : never
  : DefaultSchemaTableNameOrOptions extends keyof DefaultSchema["Tables"]
    ? DefaultSchema["Tables"][DefaultSchemaTableNameOrOptions] extends {
        Insert: infer I
      }
      ? I
      : never
    : never

export type TablesUpdate<
  DefaultSchemaTableNameOrOptions extends
    | keyof DefaultSchema["Tables"]
    | { schema: keyof DatabaseWithoutInternals },
  TableName extends DefaultSchemaTableNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals
  }
    ? keyof DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"]
    : never = never,
> = DefaultSchemaTableNameOrOptions extends {
  schema: keyof DatabaseWithoutInternals
}
  ? DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"][TableName] extends {
      Update: infer U
    }
    ? U
    : never
  : DefaultSchemaTableNameOrOptions extends keyof DefaultSchema["Tables"]
    ? DefaultSchema["Tables"][DefaultSchemaTableNameOrOptions] extends {
        Update: infer U
      }
      ? U
      : never
    : never

export type Enums<
  DefaultSchemaEnumNameOrOptions extends
    | keyof DefaultSchema["Enums"]
    | { schema: keyof DatabaseWithoutInternals },
  EnumName extends DefaultSchemaEnumNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals
  }
    ? keyof DatabaseWithoutInternals[DefaultSchemaEnumNameOrOptions["schema"]]["Enums"]
    : never = never,
> = DefaultSchemaEnumNameOrOptions extends {
  schema: keyof DatabaseWithoutInternals
}
  ? DatabaseWithoutInternals[DefaultSchemaEnumNameOrOptions["schema"]]["Enums"][EnumName]
  : DefaultSchemaEnumNameOrOptions extends keyof DefaultSchema["Enums"]
    ? DefaultSchema["Enums"][DefaultSchemaEnumNameOrOptions]
    : never

export type CompositeTypes<
  PublicCompositeTypeNameOrOptions extends
    | keyof DefaultSchema["CompositeTypes"]
    | { schema: keyof DatabaseWithoutInternals },
  CompositeTypeName extends PublicCompositeTypeNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals
  }
    ? keyof DatabaseWithoutInternals[PublicCompositeTypeNameOrOptions["schema"]]["CompositeTypes"]
    : never = never,
> = PublicCompositeTypeNameOrOptions extends {
  schema: keyof DatabaseWithoutInternals
}
  ? DatabaseWithoutInternals[PublicCompositeTypeNameOrOptions["schema"]]["CompositeTypes"][CompositeTypeName]
  : PublicCompositeTypeNameOrOptions extends keyof DefaultSchema["CompositeTypes"]
    ? DefaultSchema["CompositeTypes"][PublicCompositeTypeNameOrOptions]
    : never

export const Constants = {
  public: {
    Enums: {
      account_status: ["ACTIVE", "SUSPENDED", "BANNED", "PENDING_VERIFICATION"],
      audit_action: [
        "CREATE",
        "UPDATE",
        "DELETE",
        "LOGIN",
        "LOGOUT",
        "EXPORT_DATA",
      ],
      transaction_status: [
        "PENDING",
        "COMPLETED",
        "FAILED",
        "CANCELLED",
        "REFUNDED",
      ],
      transaction_type: ["INCOME", "EXPENSE", "TRANSFER"],
      user_role: ["MEMBER", "PREMIUM", "MODERATOR", "ADMIN"],
    },
  },
} as const
