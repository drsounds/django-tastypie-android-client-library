django-tastypie-android-client-library
======================================

A Parse like android library for interacting with tastypie django api

# Examples

1. Init the library
  
  Tasty.init("USERNAME_OR_APP_NAME", "YOUR_API_KEY", "http://url.to/api", "v1");	

2. Get some data

  TastyQuery query = new TastyQuery("product");
  
3. POST data

  TastyObject order = new TastyObject("order");
    	
  TastyObject orderline = new TastyObject("orderline");
  orderline.put("product", "/api/v1/product/1/");
  orderline.put("sku", "/api/v1/sku/test1/");
  orderline.put("count", 1);
  orderline.put("price", 1000);
  orderline.put("subtotal", 1000);
  orderline.put("merchant", "/api/v1/merchant/artistconnector/");
    	
    	List<TastyObject> orderlines = new ArrayList<TastyObject>();
    	orderlines.add(orderline);
    	order.put("orderlines", orderlines);
    	order.put("total", 1000);
    	order.put("vat", 250);
    	order.put("customer", "/api/v1/customer/9103160000/");
    	order.put("currency", "/api/v1/currency/SEK/");
    	order.put("merchant", "/api/v1/merchant/artistconnector/");
    	order.save();
