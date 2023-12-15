do language plpgsql $$
DECLARE
  rightItem varchar[];

  rightsData varchar[][] := ARRAY[
    ['Permission1', 'EDIT_ROLES'],
    ['Permission2', 'VIEW_ROLES'],
    ['Permission3', 'REMOVE_ARCHIVED_LOG']
  ];
begin
  FOREACH rightItem SLICE 1 IN ARRAY rightsData LOOP
    INSERT INTO rights_to_permission(right_id, permission_id) VALUES (rightItem[2], rightItem[1]);
  END LOOP;
end
$$;
