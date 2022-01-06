module backend {
    requires java.compiler;
    requires java.sql;
    requires lombok;
    requires org.postgresql.jdbc;
    exports gauncher.backend;
    exports gauncher.backend.v1;

}
