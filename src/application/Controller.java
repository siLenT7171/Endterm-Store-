package application;

import javafx.fxml.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class Controller implements Initializable{
	
	@FXML
    private TextField productName;

    @FXML
    private TextField productType;

    @FXML
    private TextField productCountry;

    @FXML
    private TextField productPrice;

    @FXML
    private Button buttonAdd;

    @FXML
    private Button buttonClear;

    @FXML
    private TableView<?> tableDB;

    @FXML
    private TableColumn<?, ?> tableID;

    @FXML
    private TableColumn<?, ?> tableName;

    @FXML
    private TableColumn<?, ?> tableType;

    @FXML
    private TableColumn<?, ?> tableCountry;

    @FXML
    private TableColumn<?, ?> tablePrice;

    @FXML
    private TextField productSearch;

    @FXML
    private Button buttonUpdate;

    @FXML
    private Button buttonDelete;
    
    Connection con;
	PreparedStatement pst;
	ResultSet rs;
 
	 public void Connect()
	    {
		 try {
			  Class.forName("org.postgresql.Driver");
			   
			  con = DriverManager.getConnection("jdbc:postgresql://" + "localhost" + ":" + "5432" + "/"+ "Store", "postgres", "1234");
			   
			  if(con!=null) {
				  System.out.println("Connection succeeded \n");

			  } else {
				  System.out.println("Connection failed \n");
			  }
			   	   
		   }catch(Exception e) {
			   System.out.println(e);
		   }
 
	    }

    @FXML
    void functionAdd(ActionEvent event) {
    	
    	Connect();
        String name = productName.getText();
        String type = productType.getText();
        String country = productCountry.getText();
        String price = productPrice.getText();
   try {
        pst = con.prepareStatement("Insert Into Products(product_name,type,country,price)values(?,?,?,?)");
        pst.setString(1, name);
        pst.setString(2, type);
        pst.setString(3, country);
        pst.setString(4, price);
        int status = pst.executeUpdate();
         
         if(status==1)
         { 
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Member");
            alert.setContentText("Record Addedd Successfully");
            alert.showAndWait();
            /*tableDB();*/
            productName.setText("");
            productType.setText("");
            productCountry.setText("");
            productPrice.setText("");
            productName.requestFocus();
            
         }
         else
         {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fail");
            alert.setHeaderText("Product");
            alert.setContentText("Adding Info Failed!");
            alert.showAndWait();
         }
        } 
      catch (SQLException ex)
   {
       Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
   }
    }

    @FXML
    void functionDelete(ActionEvent event) {
    	
    	   String product_id;
    				product_id  = tableID.getText();
    				
    				 try {
    						pst = con.prepareStatement("delete from book where id =?");
    				
    			            pst.setString(1, product_id);
    			            pst.executeUpdate();
    			            JOptionPane.showMessageDialog(null, "Record Delete!!!!!");
    			            /*tableDB();*/
    			           
    			            productName.setText("");
    			            productType.setText("");
    			            productCountry.setText("");
    			            productPrice.setText("");
    			            productName.requestFocus();
    					}
    	 
    		            catch (SQLException e1) {
    						
    						e1.printStackTrace();
    					}
    }

    @FXML
    void functionUpdate(ActionEvent event) {
    	String name,type,country,price,product_id;
		
		
		name = productName.getText();
		type = productType.getText();
		country = productCountry.getText();
		price = productPrice.getText();
		product_id  = tableID.getText();
		
		 try {
				pst = con.prepareStatement("update Products set product_name= ?,type = ?,country = ?,price = ? where product_id = ?");
				pst.setString(1, name);
	            pst.setString(2, type);
	            pst.setString(3, country);
	            pst.setString(4, price);
	            pst.setString(5, product_id);
	            pst.executeUpdate();
	            JOptionPane.showMessageDialog(null, "Record Update!!!!!");
	            /*tableDB();*/
	           
	            productName.setText("");
	            productType.setText("");
	            productCountry.setText("");
	            productPrice.setText("");
	            productName.requestFocus();
			}

            catch (SQLException e1) {
				
				e1.printStackTrace();
			}
    }
    
    @FXML
    void fieldSearch(ActionEvent event) {	
    	try {
	          
            String id = tableID.getText();

                pst = con.prepareStatement("select product_name,type,country,price from Products where id = ?");
                pst.setString(1, id);
                ResultSet rs = pst.executeQuery();

            if(rs.next()==true)
            {
              
                String name = rs.getString(1);
                String type = rs.getString(2);
                String country = rs.getString(3);
                String price = rs.getString(4);
                
                productName.setText(name);
                productType.setText(type);
                productCountry.setText(country);
                productPrice.setText(price);
                
                
            }   
            else
            {
            	productName.setText("");
            	productType.setText("");
                productCountry.setText("");
                productPrice.setText("");
                 
            }
            


        } 
	
	 catch (SQLException ex) {
           
        }
    }
    
    /*public void tableDB()
    {
        ObservableList<Record> Products = FXCollections.observableArrayList();
     try 
     {
         pst = con.prepareStatement("select * from Products");  
         ResultSet rs = pst.executeQuery();
    {
      while (rs.next())
      {
          Record record = new Record();
          record.setId(rs.getString(1));
          record.setName(rs.getString(2));
          record.setType(rs.getString(3));
          record.setCountry(rs.getString(4));
          record.setPrice(rs.getString(5));
          Products.add(record);
     }
  } 
              tableDB.setItems(Products);
              tableID.setCellValueFactory(f -> f.getValue().product_idProperty());
              tableName.setCellValueFactory(f -> f.getValue().product_nameProperty());
              tableType.setCellValueFactory(f -> f.getValue().typeProperty());
              tableCountry.setCellValueFactory(f -> f.getValue().countryProperty());
              tablePrice.setCellValueFactory(f -> f.getValue().priceProperty());
     }
     
     catch (SQLException ex) 
     {
         Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
     }*/
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
    	Connect();
    }
    
}
