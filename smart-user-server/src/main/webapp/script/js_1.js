/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function Orgpageselect()
{
    var className=document.getElementById("showList").className;
    if(className=="show")
        {
            document.getElementById("showList").className="hide";
            document.getElementById("edit").className="show";
        }
    else
        {
            document.getElementById("showList").className="show";
            document.getElementById("edit").className="hide";
        }
}

