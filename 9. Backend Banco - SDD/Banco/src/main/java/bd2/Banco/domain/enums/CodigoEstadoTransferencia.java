package bd2.Banco.domain.enums;

/** Códigos conocidos de cat_estado_transferencia. Umbral alto → EN_ESPERA_APROBACION; directa → EJECUTADA. */
public enum CodigoEstadoTransferencia {
    CREADA,
    EN_ESPERA_APROBACION,
    APROBADA,
    RECHAZADA,
    EJECUTADA,
    VENCIDA
}
