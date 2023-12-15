set search_path to 'auth_service';

DO LANGUAGE plpgsql $$
DECLARE
  rightsData VARCHAR [] [];
  item       VARCHAR [];
BEGIN
  rightsData = ARRAY [
  -- Клиент, полный доступ
  ['CLIENT_VIEW_CONTRACTS_LIST', 'Просмотр списка договоров'],
  ['CLIENT_VIEW_CONTRACT', 'Просмотр детальной информации по договору']
  ];

  FOREACH item SLICE 1 IN ARRAY rightsData
  LOOP
    INSERT INTO rights (externalId, description) VALUES (item [1], item [2]);
  END LOOP;
END
$$;

SELECT setup_role('Клиент, полный доступ', ARRAY [
'CLIENT_VIEW_CONTRACTS_LIST',
'CLIENT_VIEW_CONTRACT'
]);
