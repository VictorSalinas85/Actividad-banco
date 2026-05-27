export function formatDate(value?: string | null): string {
  if (!value) return '—'
  try {
    const d = new Date(value)
    if (isNaN(d.getTime())) return value
    return d.toLocaleDateString('es-CO', { year: 'numeric', month: '2-digit', day: '2-digit' })
  } catch {
    return value
  }
}

export function formatDateTime(value?: string | null): string {
  if (!value) return '—'
  try {
    const d = new Date(value)
    if (isNaN(d.getTime())) return value
    return d.toLocaleString('es-CO', {
      year: 'numeric', month: '2-digit', day: '2-digit',
      hour: '2-digit', minute: '2-digit',
    })
  } catch {
    return value
  }
}

export function formatMoney(value?: number | null, currency = 'COP'): string {
  if (value == null) return '—'
  try {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency', currency, minimumFractionDigits: 0,
    }).format(value)
  } catch {
    return String(value)
  }
}
