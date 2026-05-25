package bd2.Banco.config;

import bd2.Banco.domain.dto.response.SpResultado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Helper centralizado para invocar procedimientos almacenados con parámetros OUT.
 *
 * JUSTIFICACIÓN DE USO DE EntityManager:
 * La API JPA estándar para procedimientos almacenados con parámetros OUT es
 * StoredProcedureQuery, accesible únicamente a través de EntityManager.
 * El uso de @Procedure con @NamedStoredProcedureQuery acoplaría las entidades
 * a los SPs, violando SRP. Este helper centraliza el acceso y lo aísla del
 * dominio, manteniendo cada service independiente del mecanismo de invocación.
 */
@Component
public class ProcedureCallHelper {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Ejecuta un SP con parámetros IN simples y retorna los tres OUT estándar.
     *
     * @param nombreSp    nombre del procedimiento (ej. "sp_cta_consignar")
     * @param parametros  mapa ordenado nombre->valor de los parámetros IN
     * @return SpResultado con out_code, out_message, out_reference
     */
    public SpResultado ejecutar(String nombreSp, Map<String, Object> parametros) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery(nombreSp);

        parametros.forEach((nombre, valor) ->
                query.registerStoredProcedureParameter(nombre, resolverTipo(valor), ParameterMode.IN));

        query.registerStoredProcedureParameter("out_code", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("out_message", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("out_reference", String.class, ParameterMode.OUT);

        parametros.forEach(query::setParameter);

        query.execute();

        return SpResultado.builder()
                .code((String) query.getOutputParameterValue("out_code"))
                .message((String) query.getOutputParameterValue("out_message"))
                .reference((String) query.getOutputParameterValue("out_reference"))
                .build();
    }

    /**
     * Ejecuta un SP que retorna un ResultSet (SELECT) sin parámetros OUT.
     * Retorna directamente el StoredProcedureQuery para que el service lo procese.
     *
     * @param nombreSp   nombre del procedimiento
     * @param parametros mapa nombre->valor de parámetros IN
     * @return StoredProcedureQuery ya ejecutado, listo para getResultList()
     */
    public StoredProcedureQuery ejecutarConResultado(String nombreSp, Map<String, Object> parametros) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery(nombreSp);

        parametros.forEach((nombre, valor) ->
                query.registerStoredProcedureParameter(nombre, resolverTipo(valor), ParameterMode.IN));

        parametros.forEach(query::setParameter);

        query.execute();
        return query;
    }

    @SuppressWarnings("rawtypes")
    private Class resolverTipo(Object valor) {
        if (valor == null) return Object.class;
        return valor.getClass();
    }
}
