var adapterName = [{'NetAgent':'Network'}, {'dbmonitor':'DataBase'}, {'jvmlocal':'JVM Local'}, {'jvmremote':'JVM Remote'}, {'gmetad':'Server'}];
var selectedJson = "server"; //comboboxAll()
var menuHeading = "chartHead"; //menuHover
var host="";
var tempSelectedtab="";
var linechartdataforip;
//var linechartdata;
var listOfMetricNames=[];
var leftpanwidth;
var rightpanwidth;
var common_ResourceMetricNames = [];
var common_ResourceMetricValues = [];
var common_ResourceMetricSlaValues = [];
var common_ResourceMetricHealthStatus = [];
var common_ResourceMetricHealthCount = [];
var commmon_ResourceAlerts = [];
var commmon_ResourceAlertValues = [];
var commmon_ResourceAlertStatusNames = [];
var commmon_ResourceAlertStatusValues = [];
var commmon_ResourceAlertAssigneeNames = [];
var commmon_ResourceAlertAssigneeValues = [];
//var window1;
//var grid1;
var selectedIP = "";
//var gangaliaDownedTimestamp = "";
//var criticalMetricType = "";
var timeout_interval;
//var live_kendo_chart;
var POINTS = 500, categoryList = [], stocks;
var seriesArray = new Array();
var metriclist;
var criticalMetricList=""; //DrawLineChart
//var ss = 0;
var secoundCount = 0;
var datasourceLive;
var xAxisLabelsLive;
var yAxisValuesLive;
var MetricNameLive;
var xlablesMinutes;
var xlablesHours;
var diffTime=0;
var timeFlag=true;
//var gridDataSource;
var chartHeading = "";
var data1;
var data2;
var data3;
var data4;
var JResourceId="";
var pivotValue="";
var repeatedValue="";
//var lastvalue="";
var inc=0;
var linetimestamps=[];
var availTimestamps=[];
var resourceName=[];
var checkAdapterProgress = true;
var realMetricType="";
var menuItemSelected=[];
var serverDownList=[];
function listOfServerDown(){
var linechartdata=srReadFiles1(customerID + "/WatchDogAlert.json");
	for(var c=0;c<linechartdata.length;c++){
		if(linechartdata[c].ResourceType==selectedtab.toLowerCase() || linechartdata[c].ResourceType==selectedtab)
		serverDownList.push(linechartdata[c].ServerName);
	}
}


/** For Selecting the Menu from 1st Quarter of Chart */
function onSelect1(e) {
	var item = $(e.item);
	menuElement = item.closest(".k-menu");
	dataItem = data1[0].items;
	index = item.parentsUntil(menuElement, ".k-item").map(function () {
	    return $(this).index();
	}).get().reverse();

    index.push(item.index());

    for (var i = -1, len = index.length; ++i < len;) {
        dataItem = dataItem[index[i]];
        dataItem = i < len-1 ? dataItem.items : dataItem;
    }

    if((dataItem.value).length == 6 && (dataItem.value).indexOf('Health') == 0) {return;}
    else if((dataItem.value).length == 5 && dataItem.value.indexOf('Alert') == 0) {return;}
    else {
    	menuIndexes[0]=dataItem;
    		menuItemSelected[0] = dataItem.text;
    		listOfMetricNames[0] = dataItem.metricType;
    		$("#"+chartheaddiv[0]).val(dataItem.value);

    }

	var menu1=(dataItem.value).replace(/ /g,'');
	DrawChart(combodiv[10],menu1);
}

/** For Selecting the Menu from 2nd Quarter of Chart */
function onSelect2(e) {
	var item = $(e.item);
    menuElement = item.closest(".k-menu");
    dataItem = data2[0].items;
    index = item.parentsUntil(menuElement, ".k-item").map(function () {
    	return $(this).index();
    }).get().reverse();

    index.push(item.index());

    for (var i = -1, len = index.length; ++i < len;) {
        dataItem = dataItem[index[i]];
        dataItem = i < len-1 ? dataItem.items : dataItem;
    }
    if((dataItem.value).length == 6 && (dataItem.value).indexOf('Health') == 0) {return;}
    else if((dataItem.value).length == 5 && dataItem.value.indexOf('Alert') == 0) {return;}
    else {
    	menuIndexes[1]=dataItem;

    	menuItemSelected[1] = dataItem.text;
    		listOfMetricNames[1] = dataItem.metricType;
    		$("#"+chartheaddiv[1]).val(dataItem.value);

    }
    var menu1=(dataItem.value).replace(/ /g,'');
	DrawChart(combodiv[11],menu1);
}

/** For Selecting the Menu from 3rd Quarter of Chart */
function onSelect3(e) {
	var item = $(e.item);
    menuElement = item.closest(".k-menu");
    dataItem = data3[0].items;
    index = item.parentsUntil(menuElement, ".k-item").map(function () {
    	return $(this).index();
    }).get().reverse();

    index.push(item.index());

    for (var i = -1, len = index.length; ++i < len;) {
        dataItem = dataItem[index[i]];
        dataItem = i < len-1 ? dataItem.items : dataItem;
    }
    if((dataItem.value).length == 6 && (dataItem.value).indexOf('Health') == 0) {return;}
    else if((dataItem.value).length == 5 && dataItem.value.indexOf('Alert') == 0) {return;}
    else {
    	menuIndexes[2]=dataItem;

    		menuItemSelected[2] = dataItem.text;
    		listOfMetricNames[2] = dataItem.metricType;
    		$("#"+chartheaddiv[2]).val(dataItem.value);

    }

	var menu1=(dataItem.value).replace(/ /g,'');
	DrawChart(combodiv[12],menu1);
}

/** For Selecting the Menu from 4th Quarter of Chart */
function onSelect4(e) {
	var item = $(e.item);
	menuElement = item.closest(".k-menu");
    dataItem = data4[0].items;
    index = item.parentsUntil(menuElement, ".k-item").map(function () {
    	return $(this).index();
    }).get().reverse();

    index.push(item.index());

    for (var i = -1, len = index.length; ++i < len;) {
        dataItem = dataItem[index[i]];
        dataItem = i < len-1 ? dataItem.items : dataItem;
    }
    if((dataItem.value).length == 6 && (dataItem.value).indexOf('Health') == 0) {return;}
    else if((dataItem.value).length == 5 && dataItem.value.indexOf('Alert') == 0) {return;}
    else {
    	menuIndexes[3]=dataItem;

    		menuItemSelected[3] = dataItem.text;
    		listOfMetricNames[3] = dataItem.metricType;
    		$("#"+chartheaddiv[3]).val(dataItem.value);

    }

	var menu1=(dataItem.value).replace(/ /g,'');
	DrawChart(combodiv[13],menu1);
}

/** For converting JSON file to JSON String */
function srReadFiles1(jsonname) {
	var jsondata = $.ajax( {
		url : jsonname,
		dataType : 'json',
		global : false,
		async : false,
		success : function(data) {
			return data;
		}
	}).responseText;
	return eval(jsondata);
}

/** For Generating Menu Heading */
function comboboxesall(div1, div2, div3, div4, combodata) {
	if (selectedtab.indexOf("Business") != -1) {
		if (sub == "")
			selectedJson = "Businessservice";
		else
			selectedJson = sub;
	} else
		selectedJson = selectedtab;
	if (selectedJson == "Server")
		selectedJson = selectedJson.toLowerCase();
	combodata = srReadFiles1("jsonconfigurations/" + selectedJson
			+ "ComboSource.json");
	 data1 = combodata;
	 data2 = combodata;
	 data3 = combodata;
	 data4 = combodata;
	$(div1).kendoMenu( {

		dataSource : data1[0].items,

		select : onSelect1,

	});
	$(div2).kendoMenu( {

		dataSource : data2[0].items,

		select : onSelect2,
	});
	$(div3).kendoMenu( {

		dataSource : data3[0].items,

		select : onSelect3,
	});
	$(div4).kendoMenu( {
		dataSource : data4[0].items,
		select : onSelect4,
	});

}

/** For Displaying Red, Green, Grey image For Health in Left Grid*/
function generateHealthstatus(ResourceNames,Health) {
	if (checkAdapterProgress && JSON.stringify(Health).indexOf("CRITICAL") > 0)
		return "<img src = 'images/Red_logo.png'  title = 'Health Violated The Threshold' />";
	if (checkAdapterProgress && JSON.stringify(Health).indexOf("OK") > 0 && JSON.stringify(Health).indexOf("CRITICAL") == -1)
		return "<img src = 'images/Green_logo.png' title = 'Health OK' />";
	else
		return "<img src = 'images/Grey_logo.png' title = 'No Data' />";
}

/** For Displaying Red, Green, Grey image For Alert in Left Grid*/
function generateAlertstatus(hstdwnstatus) {
	if(JSON.stringify(hstdwnstatus).indexOf("CRITICAL") > 0)
		return "<img src = 'images/Red_logo.png' title = 'Down Event occurred' />";
	else
		return "<img src = 'images/Green_logo.png' title = 'No Down Event' />";
}

/** Change Left Grid Row text */
function generateLeftGrid(tabName,IpAddress,tabUniqueId) {
	if(tabName != 'server' && tabName != 'Desktop') {
		return IpAddress+"("+tabUniqueId+")";
	}
	else {
		$.inArray(IpAddress, serverDownList)==-1?checkAdapterProgress=true:checkAdapterProgress=false;
		return IpAddress;
	}
}

/** For Creating Left Grid */
function loadgrid(division5, gridurl, gridtitle) {
	serverDownList.length=0;
	var linechartdata=srReadFiles1(customerID + "/WatchDogAlert.json");
	for(var c=0;c<linechartdata.length;c++){
		if(linechartdata[c].ResourceType==selectedtab.toLowerCase() || linechartdata[c].ResourceType==selectedtab)
		serverDownList.push(linechartdata[c].ServerName);
	}
	displayWatchDog();
	$(division5).empty();
	dataSource = srReadFiles1(gridurl);
	//gridDataSource = dataSource;
	data1 = dataSource;
	$(division5).kendoGrid( {
		dataSource : dataSource,
		filterable : true,
		resizable : true,
		selectable : "single",
		sortable : true,
		resizable : true,
		reorderable : true,
		scrollable : {
			virtual : true
		},

		dataBound : function() {

			filenames = srReadFiles1(combourl);
			comboboxesall(combodiv[0], combodiv[1], combodiv[2], combodiv[3], filenames);
			var ip_split = $('#selectedIp').val().split(',');
			if(ip_split[1] != undefined) {
				 var selected = $(division5).data("kendoGrid").table.find("tr");
				for(var j = -1,len = selected.length; ++j < len;)
				{
					if(selectedtab=="Server" || selectedtab=="server" || selectedtab=="Desktop"){
						if($(selected[j]).text().trim()==host || $(selected[j]).text().trim()== ip_split[1].trim())
						{
							this.select($(selected[j]));
							break;
						}
					}
					else{
						if($(selected[j]).text().trim()==host+"("+JResourceId+")" || $(selected[j]).text().trim()==ip_split[1]+"("+ip_split[0]+")")
						{
							this.select($(selected[j]));
							break;
						}

					}
				}

			}
			else
				this.select(this.tbody.find(">tr:first"));
		},
		change : onChange,
		columns : [ {
			field : "ServerName",
			title : gridtitle,
			template:"#= generateLeftGrid(ResourceType,ServerName,ResourceID)#",
			filterable : true,
			width : "120px",
		},

		 {
			field : "Health",
			title : "<font color=737C7D ><b>HEALTH</b></font>",
			template:"#= generateHealthstatus(ResourceNames,Health)#",
			filterable : false,

		}, {
			field : "Alert",
			title : "<font color=737C7D ><b>ALERT</b></font>",
			template : "#= generateAlertstatus(hstdwnstatus)#",
			filterable : false,

		}

		]

	});
	/** For Displaying no-data Image */
	if (JSON.stringify(dataSource) == "[]") {
			$(combodiv[10]).css( {
				"background" : "white",
				"background-image" : "url(images/nodata.png)",
				"background-repeat" : "no-repeat",
				"background-position" : "center 30%"
			});
			$(combodiv[11]).css( {
				"background" : "white",
				"background-image" : "url(images/nodata.png)",
				"background-repeat" : "no-repeat",
				"background-position" : "center 30%"
			});
			$(combodiv[12]).css( {
				"background" : "white",
				"background-image" : "url(images/nodata.png)",
				"background-repeat" : "no-repeat",
				"background-position" : "center 30%"
			});
			$(combodiv[13]).css( {
				"background" : "white",
				"background-image" : "url(images/nodata.png)",
				"background-repeat" : "no-repeat",
				"background-position" : "center 30%"
			});
	}
}

/** On Grid load this function will call for Business View and for other included in Grid itself*/
function onDataBound() {
	filenames = srReadFiles1(combourl);
	comboboxesall(combodiv[0], combodiv[1], combodiv[2], combodiv[3], filenames);
	this.select(this.tbody.find(">tr:first"));
}



/** On Changing the Row of the grid this function will call for all tab except SLA View*/
function onChange() {
	$('#loader').html('<img src="images/loading.gif">');
    $('#loader').show();
	var selected_rowrecord = this.dataSource.view()[this.select().index()];

	$.map(this.select(), function(item) {
		selectedIP = $(item).index();
		$('#selectedIp').val(selected_rowrecord.ResourceID+","+selected_rowrecord.ServerName);
		document.cookie = 'selectedHostCookies = '+selected_rowrecord.ResourceID+","+selected_rowrecord.ServerName;
	});
	host = selected_rowrecord.ServerName;
	JResourceId=selected_rowrecord.ResourceID;
	var common_ResourceChartType=[];
	common_ResourceChartType = selected_rowrecord.ResourceChartType;
	criticalMetricList="";
	for(var c=0;c<selected_rowrecord.Health.length;c++){
		if(selected_rowrecord.Health[c]=="CRITICAL"){
			criticalMetricList=criticalMetricList+","+selected_rowrecord.ResourceNames[c];
		}
	}
	var originalmenu = data1;
	filenames = originalmenu[0].items[0].items;
	filenames1 = originalmenu[0].items[1].items;
	var w=parseInt(common_ResourceChartType[0]);
	var x=parseInt(common_ResourceChartType[1]);

	if (menuChange)
	{
		if(menuIndexes[0].category=="health")
		{
			menuItemSelected[0] = filenames[parseInt(menuIndexes[0].index)].value;
			listOfMetricNames[0] = filenames[parseInt(menuIndexes[0].index)].metricType;
			$("#"+chartheaddiv[0]).val(menuItemSelected[0]);
			DrawChart(combodiv[10],(menuItemSelected[0]).replace(/ /g,''));
		}
		else
		{
			menuItemSelected[0] = filenames1[parseInt(menuIndexes[0].index)].value;
			listOfMetricNames[0] = filenames1[parseInt(menuIndexes[0].index)].metricType;
			$("#"+chartheaddiv[0]).val(menuItemSelected[0]);
			DrawChart(combodiv[10],(menuItemSelected[0]).replace(/ /g,''));
		}
		if(menuIndexes[1].category=="health")
		{
			menuItemSelected[1] = filenames[parseInt(menuIndexes[1].index)].value;
		listOfMetricNames[1] = filenames[parseInt(menuIndexes[1].index)].metricType;
		$("#"+chartheaddiv[1]).val(menuItemSelected[1]);
		DrawChart(combodiv[11],(menuItemSelected[1]).replace(/ /g,''));
		}
		else
		{
			menuItemSelected[1] = filenames1[parseInt(menuIndexes[1].index)].value;
		listOfMetricNames[1] = filenames1[parseInt(menuIndexes[1].index)].metricType;
		$("#"+chartheaddiv[1]).val(menuItemSelected[1]);
		DrawChart(combodiv[11],(menuItemSelected[1]).replace(/ /g,''));

		}
		if(menuIndexes[2].category=="health")
		{
			menuItemSelected[2] = filenames[parseInt(menuIndexes[2].index)].value;
		listOfMetricNames[2] = filenames[parseInt(menuIndexes[2].index)].metricType;
		$("#"+chartheaddiv[2]).val(menuItemSelected[2]);
		DrawChart(combodiv[12],(menuItemSelected[2]).replace(/ /g,''));

		}
		else
		{
			menuItemSelected[2] = filenames1[parseInt(menuIndexes[2].index)].value;
		listOfMetricNames[2] = filenames1[parseInt(menuIndexes[2].index)].metricType;
		$("#"+chartheaddiv[2]).val(menuItemSelected[2]);
		DrawChart(combodiv[12],(menuItemSelected[2]).replace(/ /g,''));

		}
		if(menuIndexes[3].category=="health")
		{
		menuItemSelected[3] = filenames[parseInt(menuIndexes[3].index)].value;
		listOfMetricNames[3] = filenames[parseInt(menuIndexes[3].index)].metricType;
		$("#"+chartheaddiv[3]).val(menuItemSelected[3]);
		DrawChart(combodiv[13],(menuItemSelected[3]).replace(/ /g,''));

		}
		else
		{
			//alert(JSON.stringify(menuIndexes[3]))
			menuItemSelected[3] = filenames1[parseInt(menuIndexes[3].index)].value;
		listOfMetricNames[3] = filenames1[parseInt(menuIndexes[3].index)].metricType;
		$("#"+chartheaddiv[3]).val(menuItemSelected[3]);
		DrawChart(combodiv[13],(menuItemSelected[3]).replace(/ /g,''));

		}


	}
	else
	{
	menuIndexes[0]=filenames[w];
	menuIndexes[1]=filenames[x];
	menuIndexes[2]=filenames1[1];
	menuIndexes[3]=filenames1[2];
		//alert("from else"+JSON.stringify(menuIndexes))
		menuItemSelected[0] = filenames[w].value;
		listOfMetricNames[0] = filenames[w].metricType;
		menuItemSelected[1] = filenames[x].value;
		listOfMetricNames[1] = filenames[x].metricType;
		menuItemSelected[2] = filenames1[2].value;
		listOfMetricNames[2] = filenames1[2].metricType;
		menuItemSelected[3] = filenames1[1].value;
		listOfMetricNames[3] = filenames1[1].metricType;
		$("#"+chartheaddiv[0]).val(menuItemSelected[0]);
		$("#"+chartheaddiv[1]).val(menuItemSelected[1]);
		$("#"+chartheaddiv[2]).val(menuItemSelected[2]);
		$("#"+chartheaddiv[3]).val(menuItemSelected[3]);
	DrawChart(combodiv[10],(filenames[w].text).replace(/ /g,''));
	DrawChart(combodiv[11],(filenames[x].text).replace(/ /g,''));
	DrawChart(combodiv[12],(filenames1[2].text).replace(/ /g,''));
	DrawChart(combodiv[13],(filenames1[1].text).replace(/ /g,''));
	}


}

/** This function will call on Series Click and creating kendoGrid with data */

function summarywindow(sumarydata) {

	var summaryHeadingNames = new Array();
	var window2 = $("#summarywindow");
	window2.kendoWindow( {
		width : $(window).width()-10,
		modal : true,
		resizable: false,
		close : function() {
			//grid1.remove();
			window2 = null;
		},
		open : function() {
			$("#summarywindow").html('<div id="summarygrid"></div>');
			 $("#summarygrid").kendoGrid( {
				dataSource : new kendo.data.DataSource( {
					transport : {
						read : {
							url : "PopupController",
							dataType : "json"
						},
						parameterMap : function(options,operation) {
							return sumarydata;
						}
					},
					pageSize : 20,
					schema : {
						data : function(data) {
							var sRealMetricType='';
							if(data[0].MetricType)
								sRealMetricType=data[0].MetricType;
							else
								sRealMetricType=sumarydata.metrictype;

							 for (var key in data[0]){
								 summaryHeadingNames.push( {field : key} );
							 }
							var positioningTitle=((sRealMetricType.length+9)*5/2)+100;
							$("#summarywindow").css('min-height', '100px');
							$('.k-window').css('top', '5px');
							$('.k-window-title').html('<input type = "button"  onclick = "exportToXls()" title = "Export to XLS"	style = "background-image: url(images/exportXLS.png);cursor:pointer;" /> <input type = "button"  onclick = "exportToPdf()" title = "Export to PDF" style = "background-image: url(images/exportPDF.png);cursor:pointer;margin-right:'
														+ (($(window).width() / 2) - positioningTitle)
														+ 'px;" />Summary('+sRealMetricType.charAt(0).toUpperCase()+sRealMetricType.slice(1)+')');



							return data;
							total : data.length;
						}
					}
				}),

				serverPaging : true,
				serverSorting : true,
				filterable : true,
				sortable : true,
				resizable : true,
				pageable : true,
				height : $(window).height() - 55,
				scrollable : true,
				columns : summaryHeadingNames

			});
			$('.k-grid-pager').css('font-size', '8px');
		},
	}).data("kendoWindow").center().open();

	}


/** In Summary window, For Downloading XL Sheet*/
function exportToXls() {
	window.location.href = "DownloadMetrics?type=xls&comboboxselected=" +chartHeading;
}

/** In Summary window, For Downloading PDF*/
function exportToPdf() {
	window.location.href = "DownloadMetrics?type=pdf&comboboxselected=" + chartHeading;
}

/** Dynamically Adjusting Window Size*/
function displayAdjust() {
	$(combodiv[8]).width($(window).width() - 25);
	$(combodiv[8]).height($(window).height() - 75);
	$(combodiv[9]).width(3 * ($(combodiv[8]).width()) / 4 - 15);
	$(combodiv[9]).height($(combodiv[8]).height() - 20);
	$('#liveWindow').width(3 * ($(combodiv[8]).width()) / 4 - 15);
	$('#liveWindow').height($(combodiv[8]).height() - 20);
	$(combodiv[14]).width(1 * ($(combodiv[8]).width()) / 4 + 10);
	$(combodiv[14]).height($(combodiv[9]).height() + 20);
	$(combodiv[10]).width(($(combodiv[9]).width()) / 2 - 8);
	$(combodiv[11]).width(($(combodiv[9]).width()) / 2 - 8);
	$(combodiv[12]).width(($(combodiv[9]).width()) / 2 - 8);
	$(combodiv[13]).width(($(combodiv[9]).width()) / 2 - 8);
	$(combodiv[4]).width(($(combodiv[9]).width()) / 2 - 8);
	$(combodiv[5]).width(($(combodiv[9]).width()) / 2 - 8);
	$(combodiv[6]).width(($(combodiv[9]).width()) / 2 - 8);
	$(combodiv[7]).width(($(combodiv[9]).width()) / 2 - 8);
	$(combodiv[10]).height(($(combodiv[9]).height()) / 2 - 20);
	$(combodiv[11]).height(($(combodiv[9]).height()) / 2 - 20);
	$(combodiv[12]).height(($(combodiv[9]).height()) / 2 - 20);
	$(combodiv[13]).height(($(combodiv[9]).height()) / 2 - 20);
	$(combodiv[4]).height(($(combodiv[9]).height()) / 20);
	$(combodiv[5]).height(($(combodiv[9]).height()) / 20);
	$(combodiv[6]).height(($(combodiv[9]).height()) / 20);
	$(combodiv[7]).height(($(combodiv[9]).height()) / 20);

	leftpanwidth = 3 * ($(combodiv[8]).width()) / 4 - 15;
	rigthpanwidth = ($(combodiv[8]).width()) / 4 + 10;
	leftpanwidth = leftpanwidth+"px";
	rigthpanwidth = rigthpanwidth+"px";

}

/** On Click Refresh Button on Right side of web page.
 * This will fetch current JSON File and Generate chart again
 * */
function refreshbutton() {
	menuChange=true;
	if (selectedtab.trim() == "Business View") {

		gridurl = customerID + "/"+$('#maincombo').val()+"/"+"Defaultservice.json";
		$(combodiv[0]).empty();
		$(combodiv[1]).empty();
		$(combodiv[2]).empty();
		$(combodiv[3]).empty();

		maingrid(combodiv[14], gridurl, gridtitle);
	}
	else if (selectedtab.trim() == "SLA View") {
		displayArrange();
		TimeLinegrid();
	}
	else {
		$(combodiv[10]).empty();
		$(combodiv[11]).empty();
		$(combodiv[12]).empty();
		$(combodiv[13]).empty();

		var gridus = gridurl.split("/");
		gridurl = "";
		for ( var i = 2; i < gridus.length; i++) {
			gridurl = gridurl + "/" + gridus[i];
		}
		gridurl = customer + gridurl;

		loadgrid(combodiv[14], gridurl, gridtitle);
		//menuHower(menuHeading);
	}
	/*if(selectedtab.trim() == "Network"){
		$('#marqueeLabel').html('');
	}
	else{*/
	//displayWatchDog(linechartdata);
	/*}*/
}

/** This will Display Moving Flash Watch Dog below the Page  on Click Refresh and on tab with TimeStamp*/
/*function displayWatchDogOld(linechartdata) {
	var checkAdapterProgress = true;
	var dates=[];
	displayAdapterInformation = "";
	if(linechartdata == undefined)
		linechartdata = [];
	for(var i = -1, len = linechartdata.length; ++i < len;) {
		for(var j = -1,len1 = linechartdata[i].ResourceNames.length; ++j < len1;){
			var q=0;
			adapterName.reduce(function(keys, element){
		    for (key in element) {
		    	if(linechartdata[i].ServerName != 'null' && JSON.stringify(linechartdata[i].ResourceNames[j]).indexOf(key) != -1) {
					for(var p = -1, len2 = linechartdata[i].TimeStamps[j].length; ++p < len2;){

						dates.push(new Date(linechartdata[i].TimeStamps[j][p]));
						}
					dString = new Date(Math.max.apply(null,dates)).toString();
					var gangaliaDownedTimestamp = dString.substring(0, dString.indexOf('GMT', 0));
					gangaliaDownedServerName = linechartdata[i].ServerName;
					var NameOfMonitoringAgent=adapterName[q][key];
					displayAdapterInformation = displayAdapterInformation.trim()+";  "+gangaliaDownedServerName+" "+ NameOfMonitoringAgent +" Monitoring Agent Down at "+gangaliaDownedTimestamp;
					checkAdapterProgress = false;
					break;


		    }
		    	else{
		    		checkAdapterProgress = true;
		    	}
		    	q++;
		    }
		},[]);

		}
			for(var c = 0;c < adapterName.length; c++)
				if(linechartdata[i].ServerName != 'null' && JSON.stringify(linechartdata[i].ResourceNames[j]).indexOf(adapterName[c]) != -1) {
					for(var p=0;p<linechartdata[i].TimeStamps[j].length;p++){

						dates.push(new Date(linechartdata[i].TimeStamps[j][p]));
						}
					dString = new Date(Math.max.apply(null,dates)).toString();
					gangaliaDownedTimestamp = dString.substring(0, dString.indexOf('GMT', 0));
					gangaliaDownedServerName = linechartdata[i].ServerName;
					//var NameOfMonitoringAgent=adapterName[c].get();
					var NameOfMonitoringAgent=adapterName[c][linechartdata[i].ResourceNames[j]];
					displayAdapterInformation = displayAdapterInformation.trim()+";  "+gangaliaDownedServerName+" "+ NameOfMonitoringAgent +" Monitoring Agent Down at "+gangaliaDownedTimestamp;
					checkAdapterProgress = false;
				}
	}
	if(!checkAdapterProgress) {
		$('#marqueeLabel').css('top',$(window).height() - 30);
		$('#marqueeLabel').html('<MARQUEE WIDTH=100% BEHAVIOR="SCROLL" onmouseover="this.stop()" onmouseout="this.start()" DIRECTION="LEFT" BGColor=lightgrey>'+displayAdapterInformation.substring(1)+'</MARQUEE>');
	}else
		$('#marqueeLabel').html('');
	linechartdata = [];

}*/

function displayWatchDog() {
	var linechartdata=srReadFiles1(customerID + "/WatchDogAlert.json");
	displayAdapterInformation = "";
	if(linechartdata == undefined)
		linechartdata = [];
	if(selectedtab=="Server")
		selectedtab="server";

	for(var i = -1, len = linechartdata.length; ++i < len;) {

			dString = new Date(linechartdata[i].Timestamps).toString();
			gangaliaDownedTimestamp = dString.substring(0, dString.indexOf('GMT', 0));
			var gangaliaDownedServerName1 = linechartdata[i].ResourceType;
			var ServerName=linechartdata[i].ServerName;
			gangaliaDownedServerName=gangaliaDownedServerName1.charAt(0).toUpperCase()+gangaliaDownedServerName1.slice(1);
			displayAdapterInformation = displayAdapterInformation.trim()+";  "+ gangaliaDownedServerName +" Monitoring Agent Down on "+ ServerName +" at "+gangaliaDownedTimestamp;


		}
		if(selectedtab=="server")
		 selectedtab="Server";
		if(displayAdapterInformation.indexOf(selectedtab)!=-1){
			checkAdapterProgress = false;
		}
		else{
			checkAdapterProgress = true;
		}

	if(displayAdapterInformation != "") {
		$('#marqueeLabel').css('top',$(window).height() - 30);
		$('#marqueeLabel').html('<MARQUEE WIDTH=100% BEHAVIOR="SCROLL" onmouseover="this.stop()" onmouseout="this.start()" DIRECTION="LEFT" BGColor=lightgrey>'+displayAdapterInformation.substring(1)+'</MARQUEE>');
	}else
		$('#marqueeLabel').html('');
	linechartdata = [];
	if(selectedtab=="Server")
		 selectedtab="server";
}

function shortLabels(labelString) {

Date.prototype.monthNames = [
    "January", "February", "March",
    "April", "May", "June",
    "July", "August", "September",
    "October", "November", "December"
];
if(JSON.stringify(resourceName).indexOf("Availability")!=-1 && inc<linetimestamps.length){
	labelString=availTimestamps[inc];
	inc++;
	labelString=labelString.toString();
}

Date.prototype.getMonthName = function() {
    return this.monthNames[this.getMonth()];
};
Date.prototype.getShortMonthName = function () {
    return this.getMonthName().substr(0, 3);
};

if(labelString.toString().indexOf(":")!=-1){
	var graphunits = $("#maincombo").val();
	var valuesplit=labelString;
	var changeFormate=labelString;
	var split_Date=valuesplit.split(" ");
	if(graphunits=="Hour"){
		 var value1 = split_Date[1].split(":");
		labelString=value1[0]+":"+value1[1];

	}
	else if(graphunits=="Day"){
		 var value1 = split_Date[1].split(":");
		labelString=value1[0]+":"+value1[1];
		repeatedValue=labelString;
		 if(pivotValue==labelString){
				labelString="";
			}
			pivotValue=repeatedValue;

	}
	else if(graphunits=="Week"){
		 var value1 = split_Date[0].split("/");
			var date=new Date(changeFormate);
		 labelString=value1[2]+" "+date.getShortMonthName();
		 repeatedValue=labelString;
		 if(pivotValue==labelString){
				labelString="";
			}
			pivotValue=repeatedValue;

	}
	else if(graphunits=="Month"){
		 var value1 = split_Date[0].split("/");
		 var date=new Date(changeFormate);
		 labelString=value1[2]+" "+date.getShortMonthName();
		 repeatedValue=labelString;
		 if(pivotValue==labelString){
				labelString="";
			}
			pivotValue=repeatedValue;

	}
	else if(graphunits=="Year"){
 		var value1 = split_Date[0].split("/");
		var date=new Date(changeFormate);
		labelString=date.getShortMonthName()+", "+value1[0];
		repeatedValue=labelString;
		if(pivotValue==labelString){
			labelString="";
		}
		pivotValue=repeatedValue;

	}


	return labelString;
}
else{
	return "";
}
	}


/** On click Live button, It will generate live lineChart */
function generateLiveLineChart(id) {
	if (id.indexOf('2') > 0) {
		if (menuItemSelected[1].indexOf("Alert") != -1 || menuItemSelected[1].indexOf("Availability") != -1) {
			alert("Select from Health Menu, Except Availability");
			return;
		} else {
			menuItemSelected[0] = $("#"+chartheaddiv[1]).val();//menuItemSelected[0][1];
			listOfMetricNames[0] = listOfMetricNames[1];
		}
	}
	else if (id.indexOf('3') > 0) {
		if (menuItemSelected[2].indexOf("Alert") != -1 || menuItemSelected[2].indexOf("Availability") != -1) {
			alert("Select from Health Menu, Except Availability");
			return;
		} else {
			menuItemSelected[0] = $("#"+chartheaddiv[2]).val();//menuItemSelected[0][2];
			listOfMetricNames[0] = listOfMetricNames[2];
		}
	}
	else if (id.indexOf('4') > 0) {
		if (menuItemSelected[3].indexOf("Alert") != -1 || menuItemSelected[3].indexOf("Availability") != -1) {
			alert("Select from Health Menu, Except Availability");
			return;
		} else {
			menuItemSelected[0] = $("#"+chartheaddiv[3]).val();//menuItemSelected[3];
			listOfMetricNames[0] = listOfMetricNames[3];
		}
	}
	else if (menuItemSelected[0].indexOf("Alert") != -1 || menuItemSelected[0].indexOf("Availability") != -1) {
		alert("Select from Health Menu, Except Availability");
		return;
	}
	else{
		menuItemSelected[0] = $("#"+chartheaddiv[0]).val();//menuItemSelected[3];
	}
	var window3 = $("#liveWindow");
	window3.kendoWindow( {
	modal : true,
	resizable: false,
	close : function() {
					$("#liveChartdiv").empty();
					window3 = null;
                    clearInterval(timeout_interval);
                    window.location.reload();
                    timeout_interval = null;
                   // live_kendo_chart.remove();
	},
	open: function() {
			$("#liveWindow").html('<div id="liveChartdiv"></div>');
			initializeData();
			setTimeout(function() {
				createChartLive();
			}, 400);

		},
	}).data("kendoWindow");
	$('.k-window').css('top', '40px');
	$('.k-window').css('left', (($(window).width() - 25) / 4 + 30));
	$('.k-window-title').html(menuItemSelected[0]);
	$('.k-window-title').css('text-align','center');

}

/** Setting Metric names and calling to Generate Chart*/
function initializeData() {

         metriclist = "'"+listOfMetricNames[0].toString().replace(/,/g,"','")+"'";
         play();
	}

/** Creating Live Line Chart */
 function createChartLive() {
	 seriesArrayColor = ["green", "red","blue", "yellow","grey"];
     $("#liveChartdiv").kendoChart( {
			theme : $(document).data("kendoSkin") || "default",
			legend : {
				position: "bottom",
				visible : true
			},seriesDefaults : {
				type : "line",
				markers : {
					 size : 0
				}
			},
			series : seriesArray,
			seriesColors : seriesArrayColor,
			valueAxis : {
				labels :{template: "#= YLabelShortening(value)#"},
				/*labels : {
					format : "{0}"

				}*/
			},
			categoryAxis : {
				categories : categoryList,
				labels : {
					rotation : 90,
					step : 20

				}
			},
			transitions : false,
			tooltip : {
				visible : true,
				template : "#= series.name #= ${value}"
			}
		});
	}

 /** This function is used to call Every second by using setInterval*/
function play() {
	        timeout_interval = setInterval(addPoint,1000);
	}

/** Every second adding Values for generating live Chart*/
function addPoint() {
	var stockData;
	if (secoundCount == 5 || secoundCount == 1) {
			datasourceLive = getData();


							if(datasourceLive[0] != "[]") {
								xAxisLabelsLive = datasourceLive[0].timestamp.toString().split(':');
								yAxisValuesLive = datasourceLive[0].MetricValue;
								MetricNameLive = datasourceLive[0].MetricName;
							}
							if(secoundCount == 1) {
								if(MetricNameLive.length == 1)
									stocks = [[]];
								else if(MetricNameLive.length == 2)
									stocks = [[],[]];
								else if(MetricNameLive.length == 3)
									stocks = [[],[],[]];
								else if(MetricNameLive.length == 4)
									stocks = [[],[],[],[]];
								else if(MetricNameLive.length == 5)
									stocks = [[],[],[],[],[]];
								else if(MetricNameLive.length == 6)
									stocks = [[],[],[],[],[],[]];
						}
				secoundCount = 1;

		}
		secoundCount++;


if(xAxisLabelsLive != undefined) {
	var localTime = new Date();
	var hours = localTime.getHours();
	var minutes = localTime.getMinutes();
	var seconds = localTime.getSeconds();

	if(timeFlag){
		xlablesseconds = (parseInt(xAxisLabelsLive[2],10) + parseInt(0));
		xlablesMinutes = parseInt(xAxisLabelsLive[1],10);
		xlablesHours = parseInt(xAxisLabelsLive[0],10);
		diffTime=((hours * 3600 + minutes * 60) + seconds) - ((parseInt(xlablesHours) * 3600 + parseInt(xlablesMinutes) * 60) + parseInt(xlablesseconds));
		timeFlag = false;
	}

	var actualTime = ((parseInt(hours) * 3600 + parseInt(minutes) * 60) + parseInt(seconds)) - parseInt(diffTime);

	xhours = actualTime / 3600;
	xrem = actualTime % 3600;
	xminutes = xrem / 60;
	xseconds = xrem % 60;

	xlablesHours = singleToDoubleDigit(parseInt(xhours));
	xlablesseconds = singleToDoubleDigit(parseInt(xseconds));
	xlablesMinutes = singleToDoubleDigit(parseInt(xminutes));

	categoryList.push(xlablesHours + ":" + xlablesMinutes + ":"+ xlablesseconds);

	if (categoryList.length > POINTS) {
		categoryList.shift();
	}

	for ( var i = 0; i < stocks.length; i++) {
		stockData = stocks[i];
		seriesArray.push( {name:MetricNameLive[i],data:stocks[i],width:1});
		stockData.push(parseFloat(yAxisValuesLive[i]));
		if (stockData.length > POINTS) {
			stockData.shift();
		}
	}
}
if(xAxisLabelsLive != undefined)
	$("#liveChartdiv").data("kendoChart").refresh();
}
/** calling Servlet and get Current data for generating live Chart every 30sec */
function getData() {

		var ajax_datasource_live = $.ajax( {
			url : 'TestLineChart',
			type : 'GET',
			dataType : 'json',
			data : {
				HostName : host.replace(/ /g, ''),
				ResourceId : JResourceId.replace(/ /g, ''),
				MetricsList : listOfMetricNames[0].toString().replace(/ /g, '')
			},
			async : false,
			success : function(data) {

			}
		}).responseText;

	return eval(ajax_datasource_live);

}

/** Function For Converting Single digit to Double digit*/
function singleToDoubleDigit(a) {
	return (a+"").length == 1? "0".concat(a) : a;
}

/** Not Used*/
function menuHower(menuHeading1) {
	menuHeading=menuHeading1;
	$("#" + menuHeading1.toLowerCase()+" ul div").css('width','98%');
	$("#" + menuHeading1.toLowerCase()+" ul div").closest('ul').css('width','98%');

}

/** Not Used*/
function onExpand(e) {

	displayAdjust();
	$(combodiv[10]).data("kendoChart").redraw();
	$(combodiv[11]).data("kendoChart").redraw();
	$(combodiv[12]).data("kendoChart").redraw();
	$(combodiv[13]).data("kendoChart").redraw();
}

/** Not Used*/
function onCollapse(e) {

	$(combodiv[9]).width($(window).width() - 25);
	$(combodiv[9]).height($(window).height() - 90);
	$(combodiv[14]).width(1 * ($(combodiv[8]).width()) / 4 + 10);
	$(combodiv[14]).height($(combodiv[9]).height() + 20);
	$(combodiv[10]).width(($(combodiv[9]).width()) / 2 - 8);
	$(combodiv[11]).width(($(combodiv[9]).width()) / 2 - 8);
	$(combodiv[12]).width(($(combodiv[9]).width()) / 2 - 8);
	$(combodiv[13]).width(($(combodiv[9]).width()) / 2 - 8);
	$(combodiv[4]).width(($(combodiv[9]).width()) / 2 - 8);
	$(combodiv[5]).width(($(combodiv[9]).width()) / 2 - 8);
	$(combodiv[6]).width(($(combodiv[9]).width()) / 2 - 8);
	$(combodiv[7]).width(($(combodiv[9]).width()) / 2 - 8);
	$(combodiv[10]).height(($(combodiv[9]).height()) / 2 - 20);
	$(combodiv[11]).height(($(combodiv[9]).height()) / 2 - 20);
	$(combodiv[12]).height(($(combodiv[9]).height()) / 2 - 20);
	$(combodiv[13]).height(($(combodiv[9]).height()) / 2 - 20);
	$(combodiv[4]).height(($(combodiv[9]).height()) / 20);
	$(combodiv[5]).height(($(combodiv[9]).height()) / 20);
	$(combodiv[6]).height(($(combodiv[9]).height()) / 20);
	$(combodiv[7]).height(($(combodiv[9]).height()) / 20);
	$(combodiv[10]).data("kendoChart").redraw();
	$(combodiv[11]).data("kendoChart").redraw();
	$(combodiv[12]).data("kendoChart").redraw();
	$(combodiv[13]).data("kendoChart").redraw();
}

/** Not Used*/
function commonsplitter(splitterdivison) {
	displayAdjust();
	$(splitterdivison).kendoSplitter( {
		orientation : "horizontal",
		panes : [ {
			collapsible : true,
			size : rigthpanwidth,
			resizable : false
		}, {
			collapsible : false,
			size : leftpanwidth,
			resizable : false
		},

		],
		collapse : onCollapse,
		expand : onExpand,

	});

	loadgrid(combodiv[14], gridurl, gridtitle);
}


function DrawChart(divName,menu){
	if(menu.indexOf("TimeLine")==-1){
		if(menu.indexOf("Utilization")!=-1)
			DrawLinearGauge(divName,menu);
		else
			DrawColumnChart(divName,menu);
	}
	else{
		DrawLineChart(divName,menu);
	}
}

function DisplayNoDataImage(DivID){
	$('#loader').hide();
	$(DivID).empty();
	$(DivID).css( {
		"background" : "white",
		"background-image" : "url(images/nodata.png)",
		"background-repeat" : "no-repeat",
		"background-position" : "center 30%"
	});
}
function lineSeriesClick(menuselelected,seriesName){
	if (selectedtab == "Server") {
					selectedtab = "server";
				}
				if (selectedtab == "Business View") {
					tempSelectedtab = selectedtab;
					selectedtab = sub;
				}
				var reqdata = {
						hostName : host,
						metrictype :seriesName,
						resourcetype : selectedtab,
						ComboSelected : menuselelected,
						selectedTab : selectedtab,
						resourceId:JResourceId,
						Interval : $("#maincombo").val()
					};
				if(tempSelectedtab == "Business View") {
					selectedtab = tempSelectedtab;
					tempSelectedtab="";
				}
				chartHeading = menuselelected;
				summarywindow(reqdata);

	}

/** Creating Line Chart */

function DrawLineChart(division1,menuselelected) {
	var index=0;
	var actualMetricNames=[];
	var flagIndex=false;
	var seriesArrayColor = ["#799999", "#0c242e","#8cc7e0", "#174356","#334356"];
	if(selectedtab=="Server")
		selectedtab="server";
	if (selectedtab == "Business View") {
		tempSelectedtab = selectedtab;
		selectedtab = sub;
	}
	var linechartdataforip = srReadFiles1(customer + "/"+selectedtab+"/"+menuselelected+"/"+selectedtab+"LineChartByTime.json");
	if(selectedtab=="server")
	selectedtab="Server";
	if(tempSelectedtab == "Business View") {
		selectedtab = tempSelectedtab;
		tempSelectedtab="";
	}
	var yaxisvalues = [];
	var temp = [];
	var slaTemp = [];
	var stepLength=1;
	for(var p in linechartdataforip){
		if(linechartdataforip[p].ServerName==host && linechartdataforip[p].ResourceID==JResourceId){
			index=p;
			flagIndex=true;
			break;
		}
	}
	if(linechartdataforip[index]!=undefined && flagIndex){
		linetimestamps=[];
		linetimestamps = linechartdataforip[index].TimeStamps;
		temp = linechartdataforip[index].ResourceValues;
		slaTemp = linechartdataforip[index].SlaValues;
		resourceName = linechartdataforip[index].ResourceNames;
		actualMetricNames=linechartdataforip[index].ActualMetricTypes;
		if(JSON.stringify(resourceName).indexOf("Availability") != -1){
			availTimestamps= linechartdataforip[index].TimeStamps;
			inc=0;
		}
			stepLength=parseInt(linetimestamps[0].length/11);
			if(stepLength==0)
				stepLength=1;
			else if(stepLength==1)
				stepLength=2;
	}
	var displayMetrictype="";
	var criticalFlag=false;
	for(var k in resourceName){
		if(criticalMetricList.indexOf(resourceName[k]+",")!=-1 && (resourceName[k].toLowerCase().indexOf('max')==-1 && resourceName[k].toLowerCase().indexOf('peak')!=0)){
			realMetricType=actualMetricNames[k];
			displayMetrictype=resourceName[k];
			yaxisvalues.push( {name:resourceName[k]+" Threshold",data:slaTemp[k],width:1}, {name:resourceName[k],data:temp[k],width:1});
			seriesArrayColor = ["red","blue"];
			criticalFlag=true;
			break;
		}
	}

	if(flagIndex && !criticalFlag){
		var m=0;
		if(resourceName[m].toLowerCase().indexOf('max')!=-1 || resourceName[m].toLowerCase().indexOf('peak')!=-1){
			m=1;
		}
		realMetricType=actualMetricNames[m];
		displayMetrictype=resourceName[m];
		yaxisvalues.push( {name:resourceName[m]+" Threshold",data:slaTemp[m],width:1}, {name:resourceName[m],data:temp[m],width:1});
		seriesArrayColor = ["red","blue"];
	}
	var headingName="";
	if(realMetricType!=undefined)
	 headingName="Time Line "+realMetricType.charAt(0).toUpperCase()+realMetricType.slice(1);
	if (division1.indexOf('2') > 0) {
		$("#"+chartheaddiv[1]).val(headingName);
		listOfMetricNames[1]=displayMetrictype;
	}
	else if (division1.indexOf('3') > 0) {
		$("#"+chartheaddiv[2]).val(headingName);
		listOfMetricNames[2]=displayMetrictype;
	}
	else if (division1.indexOf('4') > 0) {
		$("#"+chartheaddiv[3]).val(headingName);
		listOfMetricNames[3]=displayMetrictype;
	}
	else{
		$("#"+chartheaddiv[0]).val(headingName);
		listOfMetricNames[0]=displayMetrictype;
	}
	if(selectedtab=="Server")
		selectedtab="server";
	if (selectedtab == "Business View") {
		tempSelectedtab = selectedtab;
		selectedtab = sub;
	}
	$(division1).click(function(){
		var headingName=$(this).parent().find('div table tr td + td').find('input').val();

		if (division1.indexOf('2') > 0 && headingName.indexOf("Time Line")!=-1) {
			lineSeriesClick(menuselelected,listOfMetricNames[1]);
			}
			else if (division1.indexOf('3') > 0 && headingName.indexOf("Time Line")!=-1) {
			lineSeriesClick(menuselelected,listOfMetricNames[2]);
			}
			else if (division1.indexOf('4') > 0 && headingName.indexOf("Time Line")!=-1) {
				lineSeriesClick(menuselelected,listOfMetricNames[3]);
			}
			else if(headingName.indexOf("Time Line")!=-1){
			lineSeriesClick(menuselelected,listOfMetricNames[0]);
			}

		});
	pivotValue="";
	if(JSON.stringify(temp)=="[]"){
		$('#loader').hide();
		DisplayNoDataImage(division1);
	}
	else if(JSON.stringify(resourceName).indexOf("Availability") != -1){
		$(division1).kendoChart({
			theme : $(document).data("kendoSkin") || "default",
			legend : {
				visible : false
			},
			seriesColors : ["green","red"],
			seriesDefaults: {
            type: "scatterLine",
            markers: {
                size: 4,
                type:"triangle"
            }
        },
        series: [{
            name: "AvailUp",
            data: temp
        },
        {
            name: "AvailDown",
            data: slaTemp
        }],
        xAxis: {
        	max:parseInt(linetimestamps.length),
        	majorUnit : 1,
            labels: {template: "#= shortLabels(value) #",rotation:320},
        },
        yAxis: {
        	max:1,
        	majorUnit : 1,
        	labels: {template: "#= shortYLabels(value) #",rotation:320},
        },
        tooltip: {
            visible: true,
            template: "#= showtooltip(value.x) #",
        }
    });
	}
	else
		$(division1).kendoChart( {
		theme : $(document).data("kendoSkin") || "default",
		legend : {
			visible : false
		},
		seriesDefaults : {
			type : "line",
			markers : {
				size : 0
			}
		},
		series : yaxisvalues,
		seriesColors : seriesArrayColor,
		valueAxis : {
			labels :{template: "#= YLabelShortening(value)#",step:2},
			majorGridLines : {
				visible : true,
			},
		},
		seriesClick : function(e) {
			e.preventDefault();
			if (selectedtab == "Server") {
				selectedtab = "server";
			}
			if (selectedtab == "Business View") {
				tempSelectedtab = selectedtab;
				selectedtab = sub;
			}
			var reqdata = {
					hostName : host,
					metrictype :e.series.name,
					resourcetype : selectedtab,
					ComboSelected : menuselelected,
					selectedTab : selectedtab,
					resourceId:JResourceId,
					Interval : $("#maincombo").val()
				};
			if(tempSelectedtab == "Business View") {
				selectedtab = tempSelectedtab;
				tempSelectedtab="";
			}
			chartHeading = menuselelected;
			summarywindow(reqdata);


		},
		categoryAxis : {
			categories :  linetimestamps[0],
			labels :{template: "#= shortLabels(value)#",rotation : 320,step : stepLength},
		},
		tooltip : {
			visible : true,
			template : "#= series.name #= ${value}"
		}
		});
	$('#loader').hide();
	if(selectedtab=="server")
		selectedtab="Server";
		if(tempSelectedtab == "Business View") {
			selectedtab = tempSelectedtab;
			tempSelectedtab="";
		}
}
function YLabelShortening(value){
	if(selectedtab=='Network' && value>(100000/4)){
		return (value/(1000000)).toFixed(1)+" M";
		if(tempSelectedtab == "Business View") {
			selectedtab = tempSelectedtab;
			tempSelectedtab="";
		}
	}
	else if(selectedtab=='JVM' && value>(104857/3)){
		return (value/(1024*1024)).toFixed(1)+" Mb";
	}
	else
		return value;
	}


function shortYLabels(value){
	if(value)
	return 'Up';
	else
	return 'Down';
	}

function showtooltip(value){
	return availTimestamps[value];
}


/** Creating Column Chart */
function DrawColumnChart(division1,menuselelected) {
	var index=0;
	var flagIndex=false;
	if(selectedtab=="Server")
		selectedtab="server";
	if (selectedtab == "Business View") {
		tempSelectedtab = selectedtab;
		selectedtab = sub;
	}
	var metricChartTypeRelation1=[];
	var common_ResourceMetricNames =[];
	JsonData1= srReadFiles1("jsonconfigurations/"+selectedtab+"ComboSource.json");
	metricChartTypeRelation1=JsonData1[0].items[0].items;

	for(var p = -1, len = metricChartTypeRelation1.length; ++p < len;){
		if(metricChartTypeRelation1[p].text.indexOf("Time Line")==-1 && menuselelected==(metricChartTypeRelation1[p].text).replace(/ /g,'')){
			common_ResourceMetricNames=metricChartTypeRelation1[p].metricType;
			break;
		}
	}

	var linechartdataforip = srReadFiles1(customer + "/"+selectedtab+"/"+menuselelected+"/"+selectedtab+"MetricTypes.json");
	if(selectedtab=="server")
		selectedtab="Server";
		if(tempSelectedtab == "Business View") {
			selectedtab = tempSelectedtab;
			tempSelectedtab="";
		}
	for(var p = -1, len1 = linechartdataforip.length; ++p < len1;){
		if(linechartdataforip[p].ServerName==host && linechartdataforip[p].ResourceID==JResourceId ){
			index=p;
			flagIndex=true;
			break;
		}
	}
	if(JSON.stringify(linechartdataforip)=="[]" || !flagIndex)
		linechartdataforip=[{ResourceNames:[],ResourceValues:[],SlaValues:[],Health:[]}];
		common_ResourceMetricNames1 = linechartdataforip[index].ResourceNames;
		common_ResourceMetricValues1 = linechartdataforip[index].ResourceValues;
		common_ResourceMetricSlaValues1 = linechartdataforip[index].SlaValues;
		common_ResourceMetricHealthStatus = linechartdataforip[index].Health;
		common_ResourceMetricValues = [];
		common_ResourceMetricSlaValues = [];
		if(JSON.stringify(common_ResourceMetricNames1) !="[]" && common_ResourceMetricNames1 != undefined){
			for(var i = -1, len2 = common_ResourceMetricNames.length; ++i < len2;){
				var mvalue=0;
				var svalue=0;
				for(var j=-1, len3 = common_ResourceMetricNames1.length; ++j < len3;){
					if(common_ResourceMetricNames[i]==common_ResourceMetricNames1[j]){
						mvalue=mvalue+common_ResourceMetricValues1[j];
						svalue=svalue+common_ResourceMetricSlaValues1[j];
					}
				}
				common_ResourceMetricValues.push(mvalue);
				common_ResourceMetricSlaValues.push(svalue);
			}
		}
		if(menuselelected.indexOf('Alert')!=-1){
			common_ResourceMetricNames=linechartdataforip[index].ResourceNames;
			common_ResourceMetricValues=linechartdataforip[index].ResourceValues;
			common_ResourceMetricSlaValues = linechartdataforip[index].SlaValues;
		}
		$(division1).empty();
		var xaxisvalues = new Array();
		var slavalues = new Array();
		var yaxisvalues = new Array();
		var heighest = 0.0;
		if(JSON.stringify(common_ResourceMetricNames) !="[]" && common_ResourceMetricNames != undefined){
			xaxisvalues=common_ResourceMetricNames;
			for ( var i = 0; i < common_ResourceMetricNames.length; i++) {
				var thresholdValue=parseFloat(common_ResourceMetricSlaValues[i]);
				if(menuselelected.indexOf('Alert')!=-1){
					thresholdValue=parseFloat(common_ResourceMetricValues[i])+10;
					if(common_ResourceMetricNames[i].match("HSTDWN"))
						thresholdValue=0;
				}
				if(parseFloat(common_ResourceMetricValues[i]) > thresholdValue)
				{
					if(common_ResourceMetricNames[i].match("Charge") || common_ResourceMetricNames[i].match("Availability")) {
						slavalues.push(parseFloat(0));
						yaxisvalues.push(parseFloat(common_ResourceMetricValues[i]));
						var temp = parseFloat(yaxisvalues[i]);
						if (temp > heighest) {
							heighest = temp;
						}
					}
					else {
						slavalues[i] = parseFloat(common_ResourceMetricValues[i]- thresholdValue);
						yaxisvalues[i] = thresholdValue;
						var temp = parseFloat(slavalues[i])+ parseFloat(yaxisvalues[i]);
						if (temp > heighest) {
							heighest = temp;
						}
					}
				}
				else if(parseFloat(common_ResourceMetricValues[i]) <= thresholdValue) {
					if(common_ResourceMetricNames[i].match("Charge") || common_ResourceMetricNames[i].match("Availability")  || common_ResourceMetricNames[i].match("HSTDWN")) {
						slavalues.push(parseFloat(common_ResourceMetricValues[i]));
						yaxisvalues.push(parseFloat(0));
						var temp = parseInt(slavalues[i]);
						if (temp > heighest) {
							heighest = temp;
						}
					}
					else {
						yaxisvalues[i] = parseFloat(common_ResourceMetricValues[i]);
						slavalues[i] = parseFloat(0);
						if (parseInt(common_ResourceMetricValues[i]) > heighest) {
							heighest = parseFloat(common_ResourceMetricValues[i]);
						}
					}
				}
			}
		}

		var maxValuewithstepArray;
		if((heighest) <= 100 && (heighest) >= 1)
			maxValuewithstepArray = {max:100,	majorGridLines : {visible : true},majorUnit : 20};
		else
			maxValuewithstepArray = {	labels :{template: "#= YLabelShortening(value)#",step:2},majorGridLines : {visible : true}};

		if(selectedtab=="Server")
			selectedtab="server";
			if (selectedtab == "Business View") {
				tempSelectedtab = selectedtab;
				selectedtab = sub;
			}

		if(JSON.stringify(yaxisvalues)=="[]"){
			$('#loader').hide();
			DisplayNoDataImage(division1);
		}
		else
			$(division1).kendoChart( {
				seriesClick : function(e) {
					e.preventDefault();
					if (selectedtab == "Server") {
						selectedtab = "server";
					}
					if (selectedtab == "Business View") {
						tempSelectedtab = selectedtab;
						selectedtab = sub;
					}
					var reqdata = {
							hostName : host,
							metrictype : e.category,
							resourcetype : selectedtab,
							ComboSelected : menuselelected,
							selectedTab : selectedtab,
							Interval : $("#maincombo").val(),
							resourceId :JResourceId
					};
					if(tempSelectedtab == "Business View") {
						selectedtab = tempSelectedtab;
						tempSelectedtab="";
					}
					chartHeading = menuselelected;
					summarywindow(reqdata);

				},
				legend : {
					visible : false
				},
				seriesDefaults : {
					type : "column",
					stack : true
				},
				series :[ {
					name : "threshold",
					data : yaxisvalues
				},
				{
					name : "crossedthreshold",
					data : slavalues
				}
				],
				seriesColors : [ "#19CF1C", "red" ],
				valueAxis : maxValuewithstepArray,
				categoryAxis : {
					categories : xaxisvalues,
					labels : {
						rotation : 320,
					}
				},
				tooltip : {
					visible : true,
					template : " ${category}" + "=" + "${value}"
				}
			});
		$('#loader').hide();
		if(selectedtab=="server")
			selectedtab="Server";
			if(tempSelectedtab == "Business View") {
				selectedtab = tempSelectedtab;
				tempSelectedtab="";
			}
}


function DrawLinearGauge(division,menuselelected) {
	var xaxisvalues = [];
	var slavalues = [];
	var yaxisvalues = [];
	$(division).empty();
	var index=0;
	var flagIndex=false;
	$(division).css('background',
			'url(lib/Default/linear-gauge-container.png) no-repeat ');
	$(division).css('backgroundPosition', 'center');

	if(selectedtab=="Server")
		selectedtab="server";
		if (selectedtab == "Business View") {
			tempSelectedtab = selectedtab;
			selectedtab = sub;
		}
	var linechartdataforip = srReadFiles1(customer + "/"+selectedtab+"/"+menuselelected+"/"+selectedtab+"MetricTypes.json");
	if(selectedtab=="server")
		selectedtab="Server";
		if(tempSelectedtab == "Business View") {
			selectedtab = tempSelectedtab;
			tempSelectedtab="";
		}
	for(var p=-1, len = linechartdataforip.length; ++p < len;){
		if(linechartdataforip[p].ServerName==host && linechartdataforip[p].ResourceID==JResourceId){
			index=p;
			flagIndex=true;
			break;
		}
	}
/*if(selectedtab=="Server")
selectedtab="server";*/
if(linechartdataforip[index]!=undefined && flagIndex){
	xaxisvalues = linechartdataforip[index].ResourceNames;
	slavalues.push(linechartdataforip[index].SlaValues);
	yaxisvalues.push(linechartdataforip[index].ResourceValues);
}
if(JSON.stringify(yaxisvalues)=="[]"){
	$('#loader').hide();
	DisplayNoDataImage(division);
}
else
	$(division).kendoLinearGauge( {
		background : "",
		pointer : {
			value : yaxisvalues[0],
			shape : "arrow",
			color : "yellow"
		},
		chartArea : {
			border : {
				color : "",
				dashType : "solid",
				width : 0
			}
		},
		scale : {
			majorUnit : 20,
			minorUnit : 2,
			min : 0,
			max : 100,
			vertical : true,
			ranges : [ {
				from : 0,
				to : slavalues[0],
				color : "green"
			}, {
				from : slavalues[0],
				to : 100,
				color : "red"
			} ]
		}
	});
$('#loader').hide();
}

function XLSReportWindow(){
    $.window({
        title:"XLS Reports",
        content:$("#DownloadXLSFiles"),
        width: 400,           // window width
        height: 320,
        showModal: true,
        modalOpacity: 0.5,
        showFooter:false,
        showRoundCorner: true,
        minimizable:false,
        maximizable:false,
        headerClass: "my_header",
        bookmarkable:false,
        scrollable:false,
        draggable:false,
        resizable:false,
        onShow: function(wnd) {
            $.ajax(
            {
                type: "POST",
                data:{
                   // sessionid:sessionid
                },
                url: "/SA-DeskUI/Load_XLSReports.action",

                success: function(submsg)
                {
                    var DIVString="<ul type='none' id='xlsattach'>";

                    var checkfilessub=$(submsg).find("name");
                    var checkfilessublength=checkfilessub.length;
                    for(var k=0;k<checkfilessublength;k++){
                        DIVString=DIVString+"<li><img src='js/images/rsz_3move_waiting_down_alternative.png' onclick='DownloadthisXLSfile(\""+$(checkfilessub[k]).text()+"\")' alt='-'/><a href=\"javascript:DownloadthisXLSfile(\'"+$(checkfilessub[k]).text()+"\')\"> "+$(checkfilessub[k]).text()+"</a></li>";
                    }
                    DIVString=DIVString+"</ul>";
                    wnd.getContainer().find(".FilesLinks").html("<div>"+DIVString+"</div>");
                }
            });
        },
        onClose: function(wnd) {}
    });
}

function DownloadthisXLSfile(filename){
    window.location.href="/SA-DeskUI/Donload_XLSfile.action?filename="+filename;
}

function closewnd(){
    $.window.closeAll();
}