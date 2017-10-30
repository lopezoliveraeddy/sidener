(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('RequestResetController', RequestResetController);

    RequestResetController.$inject = ['$timeout', 'Auth', 'LoginService'];

    function RequestResetController ($timeout, Auth, LoginService) {
        var vm = this;

        vm.error = null;
        vm.errorEmailNotExists = null;
        vm.requestReset = requestReset;
        vm.resetAccount = {};
        vm.success = null;

        vm.login = LoginService.go;

        $timeout(function (){angular.element('#email').focus();});

        function requestReset () {

            vm.error = null;
            vm.errorEmailNotExists = null;

            Auth.resetPasswordInit(vm.resetAccount.email).then(function () {
                vm.success = 'OK';
            }).catch(function (response) {
                vm.success = null;
                if (response.status === 400 && response.data === 'email address not registered') {
                    vm.errorEmailNotExists = 'ERROR';
                } else {
                    vm.error = 'ERROR';
                }
            });
        }
    }
})();
