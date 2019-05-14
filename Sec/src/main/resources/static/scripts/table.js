window.addEventListener('load', thSizer);
window.addEventListener('resize', thSizer);

function thSizer() {
	var th = document.querySelectorAll("thead tr:first-child th");
	var td = document.querySelectorAll("tbody tr:first-child td");
	for (i = 0; i < td.length; i++) {
		th[i].width = td[i].clientWidth-10 + "px";
	}
}