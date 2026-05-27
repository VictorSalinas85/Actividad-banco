import type { ReactNode } from 'react'

interface Props {
  title?: string
  description?: string
  icon?: ReactNode
}

export default function EmptyState({ title = 'No hay datos', description, icon }: Props) {
  return (
    <div className="text-center py-12 text-slate-400">
      {icon && <div className="mx-auto mb-3 opacity-60">{icon}</div>}
      <p className="font-medium text-slate-500">{title}</p>
      {description && <p className="text-sm mt-1">{description}</p>}
    </div>
  )
}
