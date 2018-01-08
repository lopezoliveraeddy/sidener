(function() {
	'use strict';
	
	angular
	    .module('sidenerApp')
	    .controller('LoadElectionController', LoadElectionController);
	
	LoadElectionController.$inject = ['$timeout', '$scope', '$stateParams','DataUtils', 'ParseLinks', 'AlertService', 'paginationConstants','Election'];
	function LoadElectionController ($timeout, $scope, $stateParams,DataUtils,ParseLinks, AlertService, paginationConstants,Election) {
		var vm = this;
		vm.election = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.loadAll = loadAll;
        vm.elections = null;
        vm.save = save;
        vm.detalle = null;
        vm.hideGrid = true;
        vm.gridOptions = {  };
        vm.gridOptions.data = null;
        
        loadAll();
        
        function loadAll () {
	        	Election.query({}, onSuccess, onError);
        }
        function onSuccess(data, headers) {
    			 vm.totalItems = headers('X-Total-Count');
             vm.elections = data;
             console.log(vm.elections);
        }
        function onError(error) {
            AlertService.error(error.data.message);
        }
        
        function save () {
        		vm.hideGrid = false;
        		vm.gridOptions.data = csv2json(vm.election.csvValue);
        		console.log(vm.gridOptions.data);
        }
        
        function procesaJson(strData, strDelimiter){
        		strDelimiter = (strDelimiter || ",");
        		strDelimiter = (strDelimiter || ",");
        	    var objPattern = new RegExp((
        	    "(\\" + strDelimiter + "|\\r?\\n|\\r|^)" +
        	    "(?:\"([^\"]*(?:\"\"[^\"]*)*)\"|" +
        	    "([^\"\\" + strDelimiter + "\\r\\n]*))"), "gi");
        	    var arrData = [[]];
        	    var arrMatches = null;
        	    while (arrMatches = objPattern.exec(strData)) {
        	        var strMatchedDelimiter = arrMatches[1];
        	        if (strMatchedDelimiter.length && (strMatchedDelimiter != strDelimiter)) {
        	            arrData.push([]);
        	        }
        	        if (arrMatches[2]) {
        	            var strMatchedValue = arrMatches[2].replace(
        	            new RegExp("\"\"", "g"), "\"");
        	        } else {
        	            var strMatchedValue = arrMatches[3];
        	        }
        	        arrData[arrData.length - 1].push(strMatchedValue);
        	    }
        	    return (arrData);
        }
        

        function csv2json(csv) {
            var array = procesaJson(csv);
            var objArray = [];
            for (var i = 1; i < array.length; i++) {
                objArray[i - 1] = {};
                for (var k = 0; k < array[0].length && k < array[i].length; k++) {
                    var key = array[0][k];
                    objArray[i - 1][key] = array[i][k]
                }
            }

            var json = JSON.stringify(objArray);
            var str = json.replace(/},/g, "},\r\n");

            return str;
        }
       
        vm.loadFile = function($file , election){
    	    if ($file) {
            DataUtils.toBase64($file, function(base64Data) {
                $scope.$apply(function() {
                	   election.dbFile = base64Data;
                    election.dbFileContentType = $file.type;
                });
            });
        }
       
    };
		
	}
	
	
})();