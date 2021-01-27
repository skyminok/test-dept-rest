Дополнительные пояснения, так как понял, что на этом стоит заострить внимание

=========
1. Выборку для объектов базы нужно сделать из представлений "organizations_user_v" и "departments_user_v"
В таком случае не нужно делать связей в классах DAO, они уже есть в представлениях БД

1.1. Так как предполагается работа с разными схемами, указание схемы в application.properties не нужно (т.е. вот такой параметр "spring.datasource.hikari.schema=") и добавить ее к Entity через

@Table( name = "departments_user_v", schema = "cdx_cims" )

И добавить определение схемы в репозиторий, для функций

...
@Procedure("cdx_cims.add_departments")

...
@Procedure("cdx_cims.delete_departments")


2. В DAO Entities добавить формат поля для дат в виде DD-MM-YYYY

####Ну и еще то, что увидел, по фильтрации.

Фронтенд будет управлять выборкой, в том числе и показ удаленных записей.
Т.е. заведомо считаем, что с фронта приходит вызов, полностью характеризующий запрос к БД.
Если в нем указано выбрать удаленные, значит они попадут в выборку.
Есть разные пути, но возможно, проще делать через JpaSpecificationExecutor и класс возвращающий спецификацию через статические методы для каждого варианта.

Например, вот так

@Repository
public interface RepositoryOrders
extends
JpaRepository<Orders, Long>,
JpaSpecificationExecutor<Orders> {
}


И спецификация


public class SpecificationOrders {
public static Specification<Orders> filterByDeleted() {
return (r, cq, cb) -> cb.equal(r.get("deleted"), 0);
}
}


Эти методы можно затем, использовать в классе сервиса, при вызове findAll( Specification... )