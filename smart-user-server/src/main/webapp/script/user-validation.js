$(document).ready(function(){  
  $("#userform").validate({
    rules: {
      firstName: "required",      
      lastName: "required",
      userName: "required",
      primaryEmail: {
        required: true,
        email:true
      },
      password: {
        required: true,
        minlength: 6
      },
      confirmPassword: {
        equalTo: "#password"
      }
    },
    messages: {
      password: "Password must be atleast of 6 characters"
    }    
  });  

  $(".submitbtn").click(function(){
    alert(1);
    var fname = $("input#fname");
    var mname = $("input#mname");
    var lname = $("input#lname");
    var password = $("input#password");
    var phone = $("input#phone");
    var datastring = 'firstName='+ fname + '&middleInitial='+ mname + '&lastName='+ lname + '&password='+ password + '&phone='+ phone;
    $.ajax({
      type: "POST",
      url: window.location,
      data: datastring,
      success: function(){
        
      }
    });
  });
  $("#userEditForm").validate({
    rules: {
      password: {
        required:true,
        minlength:6
      },
      confirmPassword:{
        required: true,        
        equalTo: "#newPassword"
      }
    }    
  });
$('#error').hide();
$("#update").click(function(){
  $('#error').hide();
  var pass = $("input#oldPassword").val();
  var pass1 = $("input#originalPassword").val();
  if(pass!=pass1)
    $("label#error").show();
  return false;
})
});

