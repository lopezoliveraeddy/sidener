(function() {
	'use strict';
	
	angular
	    .module('sidenerApp')
	    .controller('ShowElectionController', ShowElectionController);
	
	ShowElectionController.$inject = ['$timeout', '$scope', '$stateParams','DataUtils', 'ParseLinks', 'AlertService', 'paginationConstants','Election'];
	function ShowElectionController ($timeout, $scope, $stateParams,DataUtils,ParseLinks, AlertService, paginationConstants,Election) {
		var vm = this;
		
       

		
	}
	
	
})();