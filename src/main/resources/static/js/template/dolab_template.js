TEMPLATE = {
    WORKSPACE_EQM_ELEM:
        '<div class="ui-widget-content fixed draggable_item center">' +
        '    <div class="crop" style="margin-left: 11%;">'+
        '        <img src="../../../image/rest/{{id}}" class="cropimg5">\n' +
        '    </div>'+
        '   <p>{{name}}</p>' +
        '</div>',
    WORKSPACE_EQM_ELEM_BAR:
        '<div class="row sidebaritem">\n' +
        '    <div class="col s4 center">\n' +
        '        <div class="crop">'+
        '            <img src="../../../image/rest/{{equipment.id}}" class="cropimg5">\n' +
        '        </div>'+
        '    </div>\n' +
        '    <div class="col s4">\n' +
        '        <p>Name: {{equipment.name}}</p>\n' +
        '        <p>Type: {{equipment.type}}</p>\n' +
        '    </div>\n' +
        '    {{^equipment.machine}}\n' +
        '    <div class="col s4">\n' +
        '       {{#equipment.liquid}}\n' +
        '       <p>Current Volume: {{curr_val}}</p>\n' +
        '       {{/equipment.liquid}}\n' +
        '            {{#equipment.solid}}\n' +
        '       <p>Current Weight: {{curr_val}}</p>\n' +
        '       {{/equipment.solid}}\n' +
        '       <p>Current Temperature: {{equipment.props.max_temperature}}</p>\n' +
        '    </div>\n' +
        '    {{/equipment.machine}}\n' +
        '    {{#equipment.machine}}\n' +
        '    <div class="col s4"></div>\n' +
        '    {{/equipment.machine}}\n' +
        '</div>'
}
