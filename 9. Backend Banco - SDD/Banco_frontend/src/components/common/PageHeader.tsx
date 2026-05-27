import type { ReactNode } from 'react'

interface Props {
  title: string
  description?: string
  icon?: ReactNode
  actions?: ReactNode
}

export default function PageHeader({ title, description, icon, actions }: Props) {
  return (
    <div className="flex items-start justify-between mb-6 pb-4 border-b border-slate-200">
      <div className="flex items-center gap-3">
        {icon && (
          <div className="p-2 bg-blue-50 text-blue-700 rounded-lg">{icon}</div>
        )}
        <div>
          <h1 className="text-2xl font-bold text-slate-800 leading-tight">{title}</h1>
          {description && <p className="text-sm text-slate-500 mt-0.5">{description}</p>}
        </div>
      </div>
      {actions && <div className="flex items-center gap-2">{actions}</div>}
    </div>
  )
}
