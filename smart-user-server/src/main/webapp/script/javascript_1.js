/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



function Orgpageselect()
{
    
    var className=document.getElementById("showList").className;
    alert(className);
    var className1=document.getElementById("create").className;
    alert(className1);
    if(className=="show")
        {
            document.getElementById("showList").className="hide";
            document.getElementById("create").className="show";
        }
    else
        {
            document.getElementById("showList").className="show";
            document.getElementById("create").className="hide";
        }
    
}


function OnclickActive()
{
    alert("Are you sure??");

}
function submitform()
{
  alert("Potakkkkkkkkkk");
  name = document.getElementById("deleteform").name.valueOf();
  alert(name);
  document.deleteform.submit();
}

//function PostAsJson()
//{
//    $(document).ready(function(){
//    alert('Potakkkkkkkkkkkkkkkk');
//    $('form').submit(function(e) {
//    e.preventDefault();
//    $.post($(this).attr("action"), $(this).serialize(), function(json) {
//        // handle response
//    }, "json");
//    });
//   });
//}