(function() {
	'use strict';
	
	angular
	    .module('sidenerApp')
	    .controller('LoadElectionController', LoadElectionController);
	
	LoadElectionController.$inject = ['$timeout', '$scope', '$stateParams','DataUtils', 'ParseLinks', 'AlertService', 'paginationConstants','Election','LoadElection'];
	function LoadElectionController ($timeout, $scope, $stateParams,DataUtils,ParseLinks, AlertService, paginationConstants,Election,LoadElection) {
		var vm = this;
		vm.election = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.loadAll = loadAll;
        vm.elections = null;
        vm.save = save;
        vm.loadElectionDto = [];
       
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
        		if( angular.isDefined(vm.electionSel) ){
        			LoadElection.save(
        					{
        		            		 "dbFile": vm.election.dbFile,
        		            		 "dbFileContentType": vm.election.dbFileContentType,
        		            		 "eleccion" :vm.electionSel.id
        		             }
				);
        			
        		}
        		
        		
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