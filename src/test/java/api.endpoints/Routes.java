package api.endpoints;

public class Routes {

    public static String baseUrl = "https://restful-booker.herokuapp.com/booking";



    public static String GetBookingIds = baseUrl ; //using get
    public static String GetBooking = baseUrl + "/:id"; //using get

    public static String CreateBooking = baseUrl; //using post

    public static String UpdateBooking = baseUrl; //using put

    public static String PartialUpdateBooking = baseUrl + "/:id"; //using patch

    public static String DeleteBooking = baseUrl + "/1"; //delete














}
