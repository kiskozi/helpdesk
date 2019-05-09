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

function changeChecked() {
	if (document.getElementById("editOthers") == null) return;
	var disableUserCheckBox = document.getElementById("disableUser");
	var adminRoleCheckBox = document.getElementById("adminRole");
	if (disableUserCheckBox.value == "true") {
		disableUserCheckBox.checked = true;
	} else if (disableUserCheckBox.value == "false") {
		disableUserCheckBox.checked = false;
	}
	if (adminRoleCheckBox.value == "true") {
		adminRoleCheckBox.checked = true;
	} else if (adminRoleCheckBox.value == "false") {
		adminRoleCheckBox.checked = false;
	}
	return;
}

function enableDisable() {
	if(document.getElementById("disableUser").checked)
	    document.getElementById("disableUserHidden").disabled = true;
	document.getElementById("disableUserForm").submit();
	return;
}

function changeAdminRole() {
	if(document.getElementById("adminRole").checked)
	    document.getElementById("adminRoleHidden").disabled = true;
	document.getElementById("adminRoleForm").submit();
	return;
}

function chooser(obj) {
	document.getElementsByName("categoryName")[0].value = obj.innerHTML;
	document.getElementsByClassName("selectorUl")[0].style.display = "none";
}

function selectorOver() {
	document.getElementsByClassName("selectorUl")[0].style.display = "block";
}

function selectorOut() {
	document.getElementsByClassName("selectorUl")[0].style.display = "none";
}
