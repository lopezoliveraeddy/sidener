(function() {
    'use strict';

    angular
        .module('sidenerApp', [
            'ngStorage',
            'tmh.dynamicLocale',
            'pascalprecht.translate',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'ui.grid',
            'infinite-scroll',
			'ui.select',
            'ngSanitize',
            'chart.js',
            'flow',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar',
            'colorpicker.module',
            'ui.select',
            'ang-drag-drop',
            'ngclipboard'
        ])
        .run(run);

    run.$inject = ['stateHandler', 'translationHandler'];

    function run(stateHandler, translationHandler) {
        stateHandler.initialize();
        translationHandler.initialize();
    }
})();
