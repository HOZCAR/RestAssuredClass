package apiTest;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import helpers.DataHelper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.*;
import org.testng.annotations.*;
import dataEntities.Employee;

public class DummyRestApiExample {
    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;
    Employee employee;

    @BeforeTest
    public void setUp(){
        requestSpecification = new RequestSpecBuilder().
                setBaseUri("http://dummy.restapiexample.com/api/v1").setContentType(ContentType.JSON).build();
        responseSpecification = new ResponseSpecBuilder().
                expectStatusCode(200).
                expectContentType(ContentType.JSON).
                build();
    }

    @Test
    public void GetallEmployessReturns200Code(){
        //initial structure before refactor with specifications
        /*given().
                when().get("http://dummy.restapiexample.com/api/v1/employees").
                then().assertThat().
                statusCode(200).log().body();*/
        //refactor to use specficications
        given().spec(requestSpecification).
                get("employees").
                then().spec(responseSpecification).
                log().body();
    }

    @Test
    public void GetOneEmpReturnsCorrectDataAndStatusCode200(){
        //initial structure before refactor with specifications
        /*given().
                when().get("http://dummy.restapiexample.com/api/v1/employee/24").
                then().assertThat().
                body("data.employee_name", equalTo("Doris Wilder")).
                and().
                statusCode(200).
                log().body();*/
        //refactor to use specficications
        given().
                spec(requestSpecification).
                get("employee/24").
                then().
                spec(responseSpecification).
                assertThat().body("data.employee_name", equalTo("Doris Wilder")).
                log().body();
    }

    @Test
    public void CreateOneEmployee(){
        initEmployee();
        given().
                spec(requestSpecification.body(employee)).
                post("create").
        then().
                spec(responseSpecification).
                log().body();
    }

    @Test
    public void UpdateOneEmployee(){
        initEmployee();
        given()
        .when()
                .spec(requestSpecification.body(employee)).put("update/1")
        .then()
                .spec(responseSpecification).log().body();
    }

    private void initEmployee(){
        employee = new Employee(DataHelper.generateName(), DataHelper.generateRandomSalary(), DataHelper.generateRandomAge());
    }
}
