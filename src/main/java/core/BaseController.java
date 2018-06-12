package core;

import spark.Route;
import spark.Spark;
import spark.route.HttpMethod;

public abstract class BaseController implements Route {

   protected BaseController(ServiceRegistry serviceRegistry) {

       this.serviceRegistry = serviceRegistry;
   } 

    public final void init() {

        switch (getHttpMethod()) {

            case get: {
                Spark.get(getControllerUri(), this);
                break;
            }
            case post: {
                Spark.post(getControllerUri(), this);
                break;
            }
            case put: {
                Spark.put(getControllerUri(), this);
                break;
            }
            case delete: {
                Spark.delete(getControllerUri(), this);
                break;
            }
            default: throw new RuntimeException("Unable to bind controller to http method" + getHttpMethod().toString());
        }
    }

    public abstract String getControllerUri();
    public abstract HttpMethod getHttpMethod();

    protected final ServiceRegistry serviceRegistry;
}

