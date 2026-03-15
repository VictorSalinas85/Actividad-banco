create or replace function fn_masiva_simple()
returns trigger
language plpgsql
as $$
declare saldo numeric;
declare total numeric := 0;
declare r record;
begin
  if new.estado_transferencia = 'Ejecutada' and old.estado_transferencia <> 'Ejecutada' and new.tipo_transferencia = 'MASIVA' then

    select saldo_actual into saldo from cuenta_bancaria where numero_cuenta = new.cuenta_origen;

    for r in select cuenta_destino, monto from transferencia_detalle where id_transferencia = new.id_transferencia order by no_item loop
      if saldo < r.monto then
        new.estado_transferencia := 'Parcial';
        exit;
      end if;

      update cuenta_bancaria set saldo_actual = saldo_actual - r.monto where numero_cuenta = new.cuenta_origen;
      update cuenta_bancaria set saldo_actual = saldo_actual + r.monto where numero_cuenta = r.cuenta_destino;

      saldo := saldo - r.monto;
      total := total + r.monto;
    end loop;

    new.monto_total := total;
    if new.fecha_aprobacion is null then new.fecha_aprobacion := now(); end if;

  end if;

  return new;
end;
$$;

drop trigger if exists trigger_masiva_simple on transferencia;

create trigger trigger_masiva_simple
before update of estado_transferencia on transferencia
for each row
execute function fn_masiva_simple();