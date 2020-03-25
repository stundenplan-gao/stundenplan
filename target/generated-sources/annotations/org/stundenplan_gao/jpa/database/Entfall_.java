package org.stundenplan_gao.jpa.database;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Entfall.class)
public abstract class Entfall_ {

	public static volatile SingularAttribute<Entfall, Date> datum;
	public static volatile SingularAttribute<Entfall, Kurs> kurs;
	public static volatile SingularAttribute<Entfall, Integer> id;
	public static volatile SingularAttribute<Entfall, Stunde> stunde;

	public static final String DATUM = "datum";
	public static final String KURS = "kurs";
	public static final String ID = "id";
	public static final String STUNDE = "stunde";

}

