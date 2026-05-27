import type { ReactNode } from 'react'

interface Props {
  label: string
  value: string | number
  icon?: ReactNode
  tone?: 'blue' | 'green' | 'amber' | 'red' | 'slate'
}

const TONES: Record<string, string> = {
  blue:  'bg-blue-50 text-blue-700',
  green: 'bg-green-50 text-green-700',
  amber: 'bg-amber-50 text-amber-700',
  red:   'bg-red-50 text-red-700',
  slate: 'bg-slate-50 text-slate-700',
}

export default function StatCard({ label, value, icon, tone = 'blue' }: Props) {
  return (
    <div className="card flex items-center gap-4">
      {icon && <div className={`p-3 rounded-lg ${TONES[tone]}`}>{icon}</div>}
      <div>
        <p className="text-xs uppercase tracking-wide text-slate-500">{label}</p>
        <p className="text-2xl font-bold text-slate-800">{value}</p>
      </div>
    </div>
  )
}
