/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



//function Orgpageselect()
//{
//    var className=document.getElementById("showList");
//    if(className=="show")
//        {
//            document.getElementById("showList").className="hide";
//            document.getElementById("edit").className="show";
//        }
//    else
//        {
//            document.getElementById("showList").className="show";
//            document.getElementById("edit").className="hide";
//        }
//}
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