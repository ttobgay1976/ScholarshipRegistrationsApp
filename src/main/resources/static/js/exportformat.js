
	$(document).ready(function() {

		// Prevent reinitialization
		if ($.fn.DataTable.isDataTable('#mgTable')) {
			$('#mgTable').DataTable().clear().destroy();
		}

		$('#mgTable').DataTable({
			paging : true,
			searching : true,
			ordering : true,
			info : true,
			responsive : true,

			dom : 'Bfrtip',
			buttons : [ {
				extend : 'excel',
				title : 'Flood Management Report'
			}, {
				extend : 'pdf',
				title : 'Flood Management Report'
			}, {
				extend : 'print',
				title : 'Flood Management Report'
			} ]
		});
	});
