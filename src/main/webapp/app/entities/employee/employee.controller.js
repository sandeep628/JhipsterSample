(function() {
    'use strict';

    angular
        .module('newApp')
        .controller('EmployeeController', EmployeeController);

    EmployeeController.$inject = ['Employee'];

    function EmployeeController(Employee) {

        var vm = this;

        vm.employees = [];

        loadAll();

        function loadAll() {
            Employee.query(function(result) {
                vm.employees = result;
                vm.searchQuery = null;
            });
        }
    }
})();
