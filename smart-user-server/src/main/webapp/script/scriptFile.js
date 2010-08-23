/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//saumitra(start)



$(document).ready(function(){




  var url=window.location.toString();

  var prevUrl = url;
  url = url.replace("?","/frags?");
  if(url == prevUrl)
    url = url+"/frags";
  alert(url);
  $("#tablecontentid").pagination(url,'#tablecontentid');

  
  $("#uniqueShortName").keyup(function(){

    var usn =$("#uniqueShortName").val();

    $.ajax({
      type: "GET",
      url: "http://localhost:9090/orgs/shortname/"+usn,
      dataType: "xml",
      success: function(xhr){
        //                            alert('Short Name is not unique')
        $("#alertlabel").html('Short Name is not unique: try another');
      },
      error: function(xhr){
        $("#alertlabel").html('Perfect: Carry On');

      }

    });



  });
      
  $("#organizationform").validate({
    rules: {
      name: "required",
      streetAddress: "required",
      city: "required",
      state: "required",
      country:"required",
      zip:"required",


      uniqueShortName: "required"

    }


  });



});
//saumitra(end)
//Rocky(start)
$(document).ready(function(){

  $.ajax({


    type: "GET",
    url: window.location,
    datatype: "xml",

    success: function(xml){
      var contenttitle="";
      var contentid = "";

      $(xml).find('entry').each(function(){
        var title = $(this).find('title').text();

        var id = $(this).find('id').text();
        var link = $(this).find('link').attr('href');


        contenttitle += "<div class=\"title\"><a href="+ link +">" + title + "</a>";
        //alert(contenttitle)

        contentid += "<div class=\"id\">" + id + "</div>";

      });
      $("#tablecontentname-user").html(contenttitle);
      $("#tablecontentid-user").html(contentid);

      var linkvalue_next_user="";
      var linkvalue_prev_user="";
      $(xml).find('link').each(function(){

        var nextlink = $(this).attr("rel");

        if(nextlink=='next')
        {

          var href = $(this).attr("href");
          linkvalue_next_user += "<a href=" +href+ ">"+nextlink+"\t</a>";
          $("#tablecontentlink_of_next_user").html(linkvalue_next_user);

        }
        if(nextlink=='previous')
        {
          var href = $(this).attr("href");

          linkvalue_prev_user += "<a href="+href+">\t"+nextlink+"</a>";
          $("#tablecontentlink_of_prev_user").html(linkvalue_prev_user);
        }
      });

    }


  });

  $("#userform").validate({
    rules: {
      name: "required",
      midName: "required",
      lastName: "required",

      password: {
        required: true,
        minlength: 6
      },

      confirmPassword: {
        equalTo: "#password"
      },
      uniqueShortName: "required"

    },
    messages: {

      password: "Password must be atleast of 6 characters"
    }

  });


  $(".submit").click(function(){

    var fname = $("input#fname");
    var mname = $("input#mname");
    var lname = $("input#lname");
    var password = $("input#password");
    var phone = $("input#phone")
    var datastring = 'name='+ fname + '&midName='+ mname + '&lastName='+ lname + '&password='+ password + '&phone='+ phone;


  });

  $.ajax({
    type: "POST",
    url: window.location,
    data: datastring,
    success: function(){

    }
  });
  return false;

});

$(document).ready(function(){
  $(".submit").click(function(){
    var id = $("input#id");
    var name = $("input#name");
    var password = $("input#password");
    var version = $("input#version");

    var datastring = 'id='+ id +'version='+ version + 'name='+ name + '&password='+ password;


  });

  $.ajax({
    type: "POST",
    url: window.location,
    data: datastring,
    success: function(){

    }
  });
  return false;
})
