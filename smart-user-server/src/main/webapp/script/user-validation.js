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
  
})


  $("#changePasswordForm").validate({
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
    var pass = $("input#oldPassword").val();
    var pass1 = $("input#originalPassword").val();
    if(pass!=pass1){
      $("#error").show();
      //return false;
    }
  });
  $("#userEditForm").validate({
    rules: {
      fname: "required",
      lname: "required"
    }
  });

});

