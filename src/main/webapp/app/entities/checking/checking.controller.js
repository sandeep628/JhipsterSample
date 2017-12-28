(function() {
    'use strict';

    angular
        .module('newApp')
        .controller('CheckingController', CheckingController);

    CheckingController.$inject = ['Checking'];

    function CheckingController(Checking) {

        var vm = this;

        vm.checkings = [];

        loadAll();

        function loadAll() {
            Checking.query(function(result) {
                vm.checkings = result;
                vm.searchQuery = null;
            });
        }
    }
})();
