import { formatMoney } from '../../lib/formatters'

interface Props {
  value?: number | null
  currency?: string
  className?: string
}

export default function MoneyValue({ value, currency = 'COP', className }: Props) {
  return (
    <span className={`font-mono tabular-nums ${className ?? ''}`}>
      {formatMoney(value, currency)}
    </span>
  )
}
