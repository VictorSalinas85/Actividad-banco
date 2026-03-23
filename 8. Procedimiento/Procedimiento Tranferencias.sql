CREATE OR REPLACE PROCEDURE public.procedimiento_transferencias_ultimo_mes(
	IN p_id_usuario integer,
	INOUT p_cursor refcursor)
LANGUAGE 'plpgsql'
AS $BODY$
begin
    open p_cursor for
        select
            t.monto_total as monto_transferencia,
            td.cuenta_destino,
            t.fecha_creacion as fecha_transferencia
        from transferencia t
        inner join transferencia_detalle td
            on t.id_transferencia = td.id_transferencia
        where t.id_usuario_creador = p_id_usuario
          and t.fecha_creacion >= now() - interval '1 month'
        order by t.fecha_creacion desc;
end;
$BODY$;
ALTER PROCEDURE public.procedimiento_transferencias_ultimo_mes(integer, refcursor)
    OWNER TO postgres;
