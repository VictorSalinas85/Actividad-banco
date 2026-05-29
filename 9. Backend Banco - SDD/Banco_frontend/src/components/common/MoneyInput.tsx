import { useEffect, useState } from 'react'

interface Props {
  /** Valor numérico actual. '' o null cuando está vacío. */
  value: number | '' | null
  /** Emite el valor numérico (o '' si el campo queda vacío). */
  onChange: (value: number | '') => void
  className?: string
  placeholder?: string
  required?: boolean
  disabled?: boolean
  readOnly?: boolean
  id?: string
  /** Máximo de decimales permitidos (por defecto 2). */
  maxDecimals?: number
}

const nf = new Intl.NumberFormat('es-CO', { maximumFractionDigits: 2 })

/** Convierte un número a su representación es-CO (1.234.567,89). */
function toDisplay(value: number | '' | null | undefined): string {
  if (value === '' || value === null || value === undefined || Number.isNaN(value)) return ''
  return nf.format(value as number)
}

/** Interpreta una cadena con separadores es-CO y devuelve el número (o '' si vacío/ inválido). */
function parseDisplay(display: string): number | '' {
  if (!display) return ''
  const normalized = display.replace(/\./g, '').replace(',', '.')
  if (normalized === '' || normalized === '.') return ''
  const n = Number(normalized)
  return Number.isNaN(n) ? '' : n
}

/**
 * Input de dinero que va agregando los puntos de miles a medida que se digita
 * (formato colombiano: punto para miles, coma para decimales). Hacia afuera
 * siempre entrega un número, de modo que las APIs reciben el valor limpio.
 */
export default function MoneyInput({
  value, onChange, className, placeholder, required, disabled, readOnly, id, maxDecimals = 2,
}: Props) {
  const [display, setDisplay] = useState<string>(toDisplay(value))

  // Resincroniza cuando el valor llega desde afuera (reset del form, precarga de
  // edición, etc.) y no coincide con lo que el usuario está viendo.
  useEffect(() => {
    if (value === '' || value === null || value === undefined) {
      if (display !== '') setDisplay('')
      return
    }
    if (value !== parseDisplay(display)) {
      setDisplay(toDisplay(value))
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [value])

  const handle = (raw: string) => {
    // Conserva solo dígitos y una coma decimal; los puntos de miles se recalculan.
    let s = raw.replace(/[^\d,]/g, '')
    const firstComma = s.indexOf(',')
    if (firstComma !== -1) {
      s = s.slice(0, firstComma + 1) + s.slice(firstComma + 1).replace(/,/g, '')
    }

    const [intRaw, decRaw] = s.split(',')
    const intPart = intRaw.replace(/^0+(?=\d)/, '') // sin ceros a la izquierda
    const grouped = intPart === '' ? '' : nf.format(Number(intPart))

    let next = grouped
    if (decRaw !== undefined) {
      next = (grouped === '' ? '0' : grouped) + ',' + decRaw.slice(0, maxDecimals)
    }

    setDisplay(next)
    onChange(parseDisplay(next))
  }

  return (
    <input
      id={id}
      type="text"
      inputMode="decimal"
      className={className ?? 'input'}
      value={display}
      onChange={(e) => handle(e.target.value)}
      placeholder={placeholder}
      required={required}
      disabled={disabled}
      readOnly={readOnly}
    />
  )
}
