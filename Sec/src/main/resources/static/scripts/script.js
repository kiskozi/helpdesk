function markerEvent(obj) {
	if (obj.style.backgroundColor == "") {
		obj.style.backgroundColor = "rgb(125, 175, 222)";
		obj.style.borderRadius = "10px";
		obj.style.color = "white";
		return;
	}
	if (obj.style.backgroundColor == "rgb(125, 175, 222)") {
		obj.style.backgroundColor = "";
		obj.style.borderRadius = "";
		obj.style.color = "";
		return; 
	}
}

function searchBar() {
  var input, filter, ul, li, a, i;
  input = document.getElementById("search");
  filter = input.value.toUpperCase();
  ul = document.getElementById("userList");
  li = ul.getElementsByTagName("li");
  for (i = 0; i < li.length; i++) {
    a = li[i].getElementsByTagName("a")[0];
    if (a.innerHTML.toUpperCase().indexOf(filter) > -1) {
      li[i].style.display = "";
    } else {
      li[i].style.display = "none";
    }
  }
}