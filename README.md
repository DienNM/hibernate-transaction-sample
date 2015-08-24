# hibernate-transaction-sample

Example of how to configure transaction in Hibernate

- Using programmatic Transactions in Standalone app
	+ Using by default
	+ hibernate.transaction.factory_class=org.hibernate.engine.transaction.internal.jdbc.JdbcTransactionFactory
- Using programmatic Transaction With JTA:
	+ hibernate.transaction.factory_class=org.hibernate.engine.transaction.internal.jdbc.JTATransactionFactory
	+ hibernate.connection.datasource=the same datasource is configured in application server
	+ hibernate.transaction.manager_lookup_class=
		* Jboss: 		org.hibernate.transaction.JBossTransactionManagerLookup
		* Weblogic: 	org.hibernate.transaction.WeblogicTransactionManagerLookup
