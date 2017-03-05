# Paypal integration example



## Express Checkout Flow
https://developer.paypal.com/docs/integration/direct/express-checkout/integration-jsv4/checkout-flow/

## Create Paypal 
https://developer.paypal.com/docs/integration/direct/payments/paypal-payments/


## Steps of payment
STEPS:
1. create payment
After you collect the payment details from the customer, specify the payment details in a /payment call. In the request URI, set the Access-Token. In the JSON request body, set the intent to sale, the redirect URLs, and the payment_method to paypal.
A successful call returns confirmation of the transaction, with the created state and a payment ID that you can use in subsequent calls.
2. Let the user approve the payment
When you create a payment for a PayPal payment, the customer must approve the payment before you can execute the sale. To enable the customer to approve the payment, pass the id field to the payment function on your client. When the customer approves the payment, PayPal calls your client-side onAuthorize callback. PayPal passes the data.paymentID and data.payerID to your call back.
3. Execute payment
To execute the payment after the customer's approval, make a /payment/execute/ call. In the JSON request body, use the payerID value that was passed to your site. In the header, use the access token that you used when you created the payment.

Angularjs integration 
https://github.com/paypal/paypal-checkout/blob/master/demo/angular.htm