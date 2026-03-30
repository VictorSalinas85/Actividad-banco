create or replace procedure recrear_saldos_de_cuentas_bancarias()
language plpgsql
as $$
declare
    fecha_ultima_bitacora timestamp;
    
    registro_transferencia record;
    
    cursor_transferencias cursor for
        select
            t.id_transferencia,
            t.id_usuario_creador,
            t.cuenta_origen,
            td.cuenta_destino,
            td.monto,
            coalesce(t.fecha_aprobacion, t.fecha_creacion) as fecha_operacion,
            t.estado_transferencia
        from transferencia t
        inner join transferencia_detalle td
            on t.id_transferencia = td.id_transferencia
        where coalesce(t.fecha_aprobacion, t.fecha_creacion) > fecha_ultima_bitacora
          and upper(trim(t.estado_transferencia)) in ('APROBADA', 'EJECUTADA')
        order by coalesce(t.fecha_aprobacion, t.fecha_creacion), t.id_transferencia, td.no_item;
begin
    select coalesce(max(fecha_hora_operacion), timestamp '1900-01-01')
    into fecha_ultima_bitacora
    from bitacora_de_operaciones;

    open cursor_transferencias;

    loop
        fetch cursor_transferencias into registro_transferencia;
        exit when not found;

        update cuenta_bancaria
        set saldo_actual = coalesce(saldo_actual, 0) - coalesce(registro_transferencia.monto, 0)
        where numero_cuenta = registro_transferencia.cuenta_origen;

        update cuenta_bancaria
        set saldo_actual = coalesce(saldo_actual, 0) + coalesce(registro_transferencia.monto, 0)
        where numero_cuenta = registro_transferencia.cuenta_destino;

        insert into bitacora_de_operaciones (
            id_bitacora,
            tipo_operacion,
            fecha_hora_operacion,
            id_usuario,
            rol_usuario,
            id_producto_afectado,
            datos_detalle
        )
        values (
            'BIT-REPRO-' || registro_transferencia.id_transferencia || '-' ||
            replace(replace(replace(cast(clock_timestamp() as text), '-', ''), ':', ''), ' ', ''),
            'RECREACION_SALDOS_TRANSFERENCIA',
            now(),
            registro_transferencia.id_usuario_creador,
            'REPROCESO',
            cast(registro_transferencia.id_transferencia as text),
            jsonb_build_object(
                'transferencia', registro_transferencia.id_transferencia,
                'cuenta_origen', registro_transferencia.cuenta_origen,
                'cuenta_destino', registro_transferencia.cuenta_destino,
                'monto', registro_transferencia.monto,
                'fecha_operacion', registro_transferencia.fecha_operacion,
                'estado_transferencia', registro_transferencia.estado_transferencia,
                'proceso', 'recreacion_de_saldos'
            )
        );

    end loop;

    close cursor_transferencias;
end;
$$;