import Modal from '../Modal'

interface Props {
  open: boolean
  title?: string
  message: string
  confirmLabel?: string
  cancelLabel?: string
  variant?: 'default' | 'danger'
  onConfirm: () => void
  onCancel: () => void
}

export default function ConfirmDialog({
  open, title = 'Confirmar', message,
  confirmLabel = 'Aceptar', cancelLabel = 'Cancelar',
  variant = 'default', onConfirm, onCancel,
}: Props) {
  return (
    <Modal open={open} title={title} onClose={onCancel} size="sm">
      <p className="text-sm text-slate-600">{message}</p>
      <div className="flex justify-end gap-2 mt-6">
        <button onClick={onCancel} className="btn-secondary">{cancelLabel}</button>
        <button
          onClick={onConfirm}
          className={variant === 'danger' ? 'btn-primary bg-red-600 hover:bg-red-700' : 'btn-primary'}
        >
          {confirmLabel}
        </button>
      </div>
    </Modal>
  )
}
