# 02 - Bounded Contexts y Agregados

## Contextos delimitados
1. Identidad y Acceso (sec)
2. Clientes y Relacion Empresarial (cli)
3. Cuentas y Movimientos (cta)
4. Creditos (cre)
5. Transferencias y Aprobaciones (trf)
6. Auditoria y Cumplimiento (aud)

## Agregados raiz
1. Usuario
   - Entidades internas: usuario_rol
   - Invariantes: usuario debe tener estado y rol valido

2. ClientePersonaNatural
   - Entidades internas: contacto, direccion (opcional normalizacion)
   - Invariantes: identificacion unica global

3. ClienteEmpresa
   - Entidades internas: empresa_usuario
   - Invariantes: NIT unico, representante valido

4. Cuenta
   - Entidades internas: movimiento
   - Invariantes: saldo no negativo salvo sobregiro autorizado

5. Prestamo
   - Entidades internas: aprobacion, desembolso
   - Invariantes: solo analista cambia estado de decision

6. Transferencia
   - Entidades internas: aprobacion
   - Invariantes: monto > 0, estados y aprobacion segun umbral

## Reglas de consistencia por agregado
1. Cambios criticos solo por SP del agregado.
2. Lecturas optimizadas por vistas o consultas dedicadas.
3. Toda mutacion relevante emite evento de bitacora.

## Relacion entre contextos
- sec provee identidad/autorizacion a todos.
- cli provee titularidad para cta, cre, trf.
- cta soporta impacto de cre y trf sobre saldos.
- aud recibe eventos de todos los contextos.
