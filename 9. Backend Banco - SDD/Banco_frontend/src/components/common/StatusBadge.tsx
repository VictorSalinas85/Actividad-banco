interface Props {
  label: string
  status: string
}

const PALETTE: Record<string, string> = {
  ACTIVO:    'bg-green-100 text-green-800',
  ACTIVA:    'bg-green-100 text-green-800',
  APROBADO:  'bg-green-100 text-green-800',
  EJECUTADA: 'bg-green-100 text-green-800',
  PENDIENTE: 'bg-amber-100 text-amber-800',
  EN_REVISION: 'bg-amber-100 text-amber-800',
  BLOQUEADO: 'bg-red-100 text-red-800',
  BLOQUEADA: 'bg-red-100 text-red-800',
  CANCELADO: 'bg-slate-200 text-slate-700',
  CANCELADA: 'bg-slate-200 text-slate-700',
  RECHAZADO: 'bg-red-100 text-red-800',
  RECHAZADA: 'bg-red-100 text-red-800',
  INACTIVO:  'bg-slate-200 text-slate-700',
  INACTIVA:  'bg-slate-200 text-slate-700',
}

export default function StatusBadge({ label, status }: Props) {
  const cls = PALETTE[status?.toUpperCase()] ?? 'bg-slate-100 text-slate-700'
  return (
    <span className={`inline-flex items-center text-xs font-semibold px-2 py-0.5 rounded-full ${cls}`}>
      {label}
    </span>
  )
}
