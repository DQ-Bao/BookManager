package Controllers;

import Annotations.Authorize;
import Models.Category;
import Models.SubCategory;
import DataAccesses.CategoryDataAccess;
import DataAccesses.Internal.DBProps;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CategoryController extends HttpServlet {
    private CategoryDataAccess categoryDAO;
    
    @Override
    public void init() throws ServletException {
        String driverName = getServletContext().getInitParameter("db-driver");
        String connectionString = getServletContext().getInitParameter("db-connection-string");
        this.categoryDAO = CategoryDataAccess.getInstance(new DBProps(driverName, connectionString));
    }
    
    @Override
    @Authorize({"Admin"})
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            resp.sendError(400);
            return;
        }
        if (action.equals("add-category")) {
            String name = req.getParameter("category-name");
            categoryDAO.addOneCategory(Category.getBuilder().Name(name).Build());
        }
        else if (action.equals("add-subCategory")) {
            String name = req.getParameter("subCategory-name");
            int parentId = Integer.parseInt(req.getParameter("subCategory-parentId"));
            categoryDAO.addOneSubCategory(SubCategory.getBuilder().Name(name).ParentId(parentId).Build());
        }
        resp.sendRedirect(req.getContextPath() + "/Admin");
    }
}
