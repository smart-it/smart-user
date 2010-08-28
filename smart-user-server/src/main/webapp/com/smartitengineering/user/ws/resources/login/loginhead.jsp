<%-- 
    Document   : loginhead
    Created on : Aug 25, 2010, 10:51:40 AM
    Author     : saumitra
--%>

<script>
  $(document).ready(function(){
  ("#username").(function(){
    var username=("#loginnametextbox").text();
    var pass=("#loginpasstextbox").text();
    alert(username);


      $.ajax({
        type: "GET",
        url: "http://localhost:9090/orgs/shortname/"+usn,
        dataType: "xml",
        success: function(xhr){
          
        },
        error: function(xhr){
          
        }
      });
    });
    });
</script>
