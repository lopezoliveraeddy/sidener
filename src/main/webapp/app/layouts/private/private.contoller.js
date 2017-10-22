(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('PrivateController', PrivateController);

    PrivateController.$inject = ['$state', '$translate', 'Auth', 'Principal', 'ProfileService', 'LoginService'];

    function PrivateController($state, $translate, Auth, Principal, ProfileService, LoginService) {
        var vm = this;

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        vm.isAdmin = Principal.hasAuthority('ROLE_ADMIN').then(function(result) {
            return result;
        });

        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;

        vm.menu = [{
            name: $translate.instant('global.menu.home'),
            action: function() {
                $state.go('home');
            },
            icon: 'glyphicon glyphicon-home',
            hasChild: false,
            isOpen: false,
            isActive: false,
            isVisible: true
        }, {
            name: $translate.instant('global.menu.entities.main'),
            icon: 'glyphicon glyphicon-th-list',
            action: function() {
                return;
            },
            hasChild: true,
            isOpen: false,
            isActive: false,
            isVisible: true,
            childrens: [{
                name: $translate.instant('global.menu.entities.election'),
                action: function() {
                    $state.go('election');
                },
                icon: 'glyphicon glyphicon-asterisk',
                isActive: false,
                isVisible: true
            }, {
                name: $translate.instant('global.menu.entities.politicalParty'),
                action: function() {
                    $state.go('political-party');
                },
                icon: 'glyphicon glyphicon-asterisk',
                isActive: false,
                isVisible: true
            }, {
                name: $translate.instant('global.menu.entities.independentCandidate'),
                action: function() {
                    $state.go('independent-candidate');
                },
                icon: 'glyphicon glyphicon-asterisk',
                isActive: false,
                isVisible: true
            }, {
                name: $translate.instant('global.menu.entities.coalition'),
                action: function() {
                    $state.go('coalition');
                },
                icon: 'glyphicon glyphicon-asterisk',
                isActive: false,
                isVisible: true
            }, {
                name: $translate.instant('global.menu.entities.causal'),
                action: function() {
                    $state.go('causal');
                },
                icon: 'glyphicon glyphicon-asterisk',
                isActive: false,
                isVisible: true
            }, {
                name: $translate.instant('global.menu.entities.electionType'),
                action: function() {
                    $state.go('election-type');
                },
                icon: 'glyphicon glyphicon-asterisk',
                isActive: false,
                isVisible: true
            }, {
                name: $translate.instant('global.menu.entities.district'),
                action: function() {
                    $state.go('district');
                },
                icon: 'glyphicon glyphicon-asterisk',
                isActive: false,
                isVisible: true
            }, {
                name: $translate.instant('global.menu.entities.pollingPlace'),
                action: function() {
                    $state.go('polling-place');
                },
                icon: 'glyphicon glyphicon-asterisk',
                isActive: false,
                isVisible: true
            }, {
                name: $translate.instant('global.menu.entities.vote'),
                action: function() {
                    $state.go('vote');
                },
                icon: 'glyphicon glyphicon-asterisk',
                isActive: false,
                isVisible: true
            }]
        }, {
            name: $translate.instant('global.menu.account.main'),
            icon: 'glyphicon glyphicon-user',
            action: function() {
                return;
            },
            hasChild: true,
            isOpen: false,
            isActive: false,
            isVisible: true,
            childrens: [{
                name: $translate.instant('global.menu.account.settings'),
                action: function() {
                    $state.go('settings');
                },
                icon: 'glyphicon glyphicon-wrench',
                isActive: false,
                isVisible: true
            }, {
                name: $translate.instant('global.menu.account.password'),
                action: function() {
                    $state.go('password');
                },
                icon: 'glyphicon glyphicon-lock',
                isActive: false,
                isVisible: true
            }, {
                name: $translate.instant('global.menu.account.logout'),
                action: logout,
                icon: 'glyphicon glyphicon-log-out',
                isActive: false,
                isVisible: true
            }, {
                name: $translate.instant('global.menu.account.login'),
                action: login,
                icon: 'glyphicon glyphicon-log-in',
                isActive: false,
                isVisible: true
            }]
        }, {
            name: $translate.instant('global.menu.admin.main'),
            icon: 'glyphicon glyphicon-tower',
            action: function() {
                return;
            },
            hasChild: true,
            isOpen: false,
            isActive: false,
            isVisible: vm.isAdmin,
            childrens: [{
                name: $translate.instant('global.menu.admin.userManagement'),
                action: function() {
                    $state.go('user-management');
                },
                icon: 'glyphicon glyphicon-user',
                isActive: false,
                isVisible: true
            }, {
                name: $translate.instant('global.menu.admin.metrics'),
                action: function() {
                    $state.go('jhi-metrics');
                },
                icon: 'glyphicon glyphicon-dashboard',
                isActive: false,
                isVisible: true
            }, {
                name: $translate.instant('global.menu.admin.health'),
                action: function() {
                    $state.go('jhi-health');
                },
                icon: 'glyphicon glyphicon-heart',
                isActive: false,
                isVisible: true
            }, {
                name: $translate.instant('global.menu.admin.configuration'),
                action: function() {
                    $state.go('jhi-configuration');
                },
                icon: 'glyphicon glyphicon-list-alt',
                isActive: false,
                isVisible: true
            }, {
                name: $translate.instant('global.menu.admin.audits'),
                action: function() {
                    $state.go('audits');
                },
                icon: 'glyphicon glyphicon-bell',
                isActive: false,
                isVisible: true
            }, {
                name: $translate.instant('global.menu.admin.logs'),
                action: function() {
                    $state.go('logs');
                },
                icon: 'glyphicon glyphicon-tasks',
                isActive: false,
                isVisible: true
            }, {
                name: $translate.instant('global.menu.admin.apidocs'),
                action: function() {
                    $state.go('docs');
                },
                icon: 'glyphicon glyphicon-book',
                isActive: false,
                isVisible: vm.swaggerEnabled
            }, {
                name: $translate.instant('global.menu.admin.database'),
                action: function() {
                    window.location = '/h2-console';
                },
                icon: 'glyphicon glyphicon-hdd',
                isActive: false,
                isVisible: !vm.inProduction
            }]
        }];

        function login() {
            collapseNavbar();
            LoginService.open();
        }

        function logout() {
            collapseNavbar();
            Auth.logout();
            $state.go('home');
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }
    }
})();
