import com.google.gson.Gson;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import spark.Spark;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static spark.Spark.get;
import static spark.Spark.post;

public class Main {
    public static void main(String[] args) {

        String clientID = "AcBXfMavHCM3dequcdwz5leE8xP6OGZ3rQ4oSO5uAdcLlvp_MdBMHLp_GigBa8e9t_8PWA-y4ITDMKg-";
        String clientSecret ="EFozFGirKiS1ecDTCykRcrcPZhQ0AL59UK1l47Uh1mXORzxqPGHu5cjOBeYLBDPNGdGTAc_SBPkdpOD0";
        String mode ="sandbox";
        Spark.staticFileLocation("/public");
        Spark.exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
        });

        post("/api/paypal/create-payment", (req, res) -> {
            // ###Details
            // Let's you specify details of a payment amount.
            Details details = new Details();
            details.setSubtotal("8.1");
            details.setTax("1.9");

            // ###Amount
            // Let's you specify a payment amount.
            Amount amount = new Amount();
            amount.setCurrency("EUR");
            // Total must be equal to sum of shipping, tax and subtotal.
            amount.setTotal("10");
            //amount.setDetails(details);

            // ###Transaction
            // A transaction defines the contract of a
            // payment - what is the payment for and who
            // is fulfilling it. Transaction is created with
            // a `Payee` and `Amount` types
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction
                    .setDescription("This is the payment transaction description.");

            // ### Items
            Item item = new Item();
            item.setName("Ground Coffee 40 oz").setQuantity("1").setCurrency("EUR").setPrice("10");
            ItemList itemList = new ItemList();
            List<Item> items = new ArrayList<Item>();
            items.add(item);
            itemList.setItems(items);

            transaction.setItemList(itemList);


            // The Payment creation API requires a list of
            // Transaction; add the created `Transaction`
            // to a List
            List<Transaction> transactions = new ArrayList<Transaction>();
            transactions.add(transaction);

            // ###Payer
            // A resource representing a Payer that funds a payment
            // Payment Method
            // as 'paypal'
            Payer payer = new Payer();
            payer.setPaymentMethod("paypal");

            // ###Payment
            // A Payment Resource; create one using
            // the above types and intent as 'sale'
            Payment payment = new Payment();
            payment.setIntent("sale");
            payment.setPayer(payer);
            payment.setTransactions(transactions);

            // ###Redirect URLs
            RedirectUrls redirectUrls = new RedirectUrls();
            String guid = UUID.randomUUID().toString().replaceAll("-", "");
            redirectUrls.setCancelUrl("localhost:4567/paymentwithpaypal?guid=" + guid);
            redirectUrls.setReturnUrl("localhost:4567/paymentwithpaypal?guid=" + guid);
            payment.setRedirectUrls(redirectUrls);
            Payment createdPayment = null;
            try {
                APIContext apiContext = new APIContext(clientID, clientSecret, mode);

                createdPayment = payment.create(apiContext);
                res.type("application/json");
                return "{\"token\":\""+createdPayment.getId()+"\"}";
            } catch ( Exception e) {
                e.printStackTrace();
            }
            return createdPayment;

        });

        post("/api/paypal/execute-payment", (req, res) ->{

            Payment payment = new Payment();
            payment.setId(req.queryParams("paymentID"));
            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(req.queryParams("payerID"));

            try {
                APIContext apiContext = new APIContext(clientID, clientSecret, mode);

                Payment createdPayment = payment.execute(apiContext, paymentExecution);
            } catch ( Exception e) {
                e.printStackTrace();
            }
            res.type("application/json");
            res.status(201);
            return "{}";
        });


    }
}