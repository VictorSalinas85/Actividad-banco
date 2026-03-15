create or replace function fn_detener_si_sin_fondos()
returns trigger
language plpgsql
as $$
declare total numeric;
declare saldo numeric;
begin
  if new.estado_transferencia = 'Ejecutada' and old.estado_transferencia <> 'Ejecutada' and new.tipo_transferencia = 'SIMPLE' then
    select coalesce(sum(monto),0) into total from transferencia_detalle where id_transferencia = new.id_transferencia;
    select saldo_actual into saldo from cuenta_bancaria where numero_cuenta = new.cuenta_origen;
    if saldo < total then
      raise exception 'sin fondos: no se puede ejecutar esta transferencia, se detiene la aprobacion';
    end if;
  end if;
  return new;
end;
$$;

drop trigger if exists trigger_detener_si_sin_fondos on transferencia;

create trigger trigger_detener_si_sin_fondos
before update of estado_transferencia on transferencia
for each row
execute function fn_detener_si_sin_fondos();