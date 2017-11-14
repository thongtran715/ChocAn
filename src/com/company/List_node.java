package com.company;

/**
 * Created by ThongTran on 11/10/17.
 */
import javax.xml.ws.Service;
import java.rmi.registry.LocateRegistry;
import java.util.*;
import java.time.*;

// Enum Type to specify the type of the List
 enum List_Type {
    Provider_Provided, Provider_All_Services, Customer_Received
}
  class Services {
    public Services() {
        name_services = " "; services_fee = 0.0; services_code = " ";
    }
    public  Services( String name, String code, double fee) { this.name_services = name; this.services_code = code; this.services_fee = fee;}
    protected String name_services;
    protected String services_code;
    protected double services_fee;
    public void display_service () {
        System.out.println(" The Name of the Service: " + name_services);
        System.out.println(" The Code of the Service: "  +  services_code);
        System.out.println(" The Service Fee: " + services_fee);
    }

}
class Services_Provided extends  Services{
    public Services_Provided() {
        time_services = null;
    }
    public Services_Provided(  LocalDate time){ this.time_services = time;}
    protected LocalDate time_services;
    @Override
    public void display_service () {
        System.out.println(" The Date: " + time_services);
    }

}
public class List_node {
    public List_node()
    {
        this.next = null;
    }
    public void display_items () {
        data.display_service();
    }
    public List_node(Services data) {
        this.data = data;
    }
    public Services data;
    public List_node next;

}
class List{
    private List_node head;
    private List_node tail;
    private List_Type type;
    public List () {
        this.head = null;
    }
    public List ( List_Type type) {
        this();
        this.type = type;
    }
    public void add_service_to_list (Services to_add)
    {
        switch (type)
        {
            case Provider_All_Services:
                add_all_available_services_for_provider(to_add);
                break;
            case Provider_Provided:
                add_all_services_provided_for_provider(to_add);
                break;
            case Customer_Received:
                add_list_services_received_for_customer(to_add);
                break;
                default:
                    break;
        }
    }

    private void add_all_services_provided_for_provider ( Services to_add){
            add_all_available_services_for_provider(to_add);
    }
    private void add_all_available_services_for_provider (Services to_add) {
        List_node newNode = new List_node(to_add) ;
        if ( head == null){
            head = newNode;
            tail = head;
            return;
        }
        else
        {
            tail.next = newNode;
            tail = newNode;
            return;
        }

    }
    private void add_list_services_received_for_customer ( Services to_add) {

        List_node newNode = new List_node(to_add);
        // It is being upcasted in main
        Services_Provided to_data = (Services_Provided) to_add;
        if ( head == null)
        {

            newNode.next = head;
            head = newNode;
            tail = head;
            return;
        }
        else if ( head != null)
        {
            Services_Provided data = (Services_Provided)(head.data);
          if (data.time_services.isBefore(to_data.time_services))
            {
                newNode.next = head;
                head = newNode;
                tail = head;
                return;
            }
            return;
        }
        else
        {
            List_node curr = head;
            while (curr.next != null ) {
                curr.next.data = new Services_Provided();
                Services_Provided to_check = new Services_Provided();
                if(to_check.time_services.isAfter(to_data.time_services))
                curr = curr.next;
            }
            newNode.next = curr.next;
            curr.next = newNode;
            tail = newNode.next;
            return;
        }
    }

    public void display_list_services_(){
        if ( head == null)
        {
            System.out.println("The List of services provided is currently empty");
            return;
        }
        else
            display_list_services(this.head);
    }
    private void display_list_services ( List_node curr)
    {
        if(curr == null) return;
        curr.display_items();
        display_list_services(curr.next);
    }

    public static void main(String[] args) {
	// write your code here
        List customer_list = new List(List_Type.Customer_Received);
        Services data = new Services_Provided(LocalDate.of(1000,3,4));
        Services data2 = new Services_Provided(LocalDate.of(2017, 11, 3)) ;
        Services data3 = new Services_Provided(LocalDate.of(2017, 11, 30)) ;
        customer_list.add_service_to_list(data);
        customer_list.add_service_to_list(data2);
        customer_list.add_service_to_list(data3);
        customer_list.display_list_services_();


        List provider_list = new List(List_Type.Provider_Provided);
        Services data4 = new Services("Service 1", "01223", 23) ;
        Services data5 = new Services("Service 1", "01223", 23) ;
        Services data6 = new Services("Service 1", "01223", 23) ;
        provider_list.add_service_to_list(data4);
        provider_list.add_service_to_list(data5);
        provider_list.add_service_to_list(data6);
        provider_list.display_list_services_();
    }
}
