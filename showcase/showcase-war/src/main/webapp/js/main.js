/**
 * Created by sphinx on 29/09/14.
 */

function Hello($scope, $http) {
    $http.get('http://localhost:8080/showcase/rest/person').
        success(function(data) {
            $scope.person = data;
        });
}