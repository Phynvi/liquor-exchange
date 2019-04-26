import React, {FC, useState, useEffect} from 'react';

import {Collection, loadDocuments} from '../client/actions';
import {SelectRaw} from './Select';
import {withForm} from './Form';

interface IProps {
	value?: any | null;
	onChange: (value: any) => void;
	isInvalid?: boolean;
	htmlId?: string;
	collection: Collection;
	idFieldName: string;
	labelFieldName: string;
	assignObject?: boolean;
	allowEmpty?: boolean;
	emptyOptionName?: string;
}

/**
 * Tato komponenta slouží k výběru dokumentu z kolekce - automaticky načte celou kolekci z REST API.
 */
export const SelectDocumentRaw: FC<IProps> = ({
	value,
	onChange,
	isInvalid,
	htmlId,
	collection,
	idFieldName,
	labelFieldName,
	assignObject,
	allowEmpty = false,
	emptyOptionName = 'None',
}) => {
	const [items, setItems] = useState<object[] | null>(null);

	const extractCollectionName = (path: string) => {
		const pieces = path.split('/');
		return pieces[pieces.length - 1];
	};

	const loadItems = () => {
		loadDocuments(collection).then(response => {
			const newItems: object[] = response.entity._embedded[extractCollectionName(collection)];
			if (allowEmpty) {
				newItems.unshift({
					[idFieldName]: '',
					[labelFieldName]: emptyOptionName,
				});
			}
			setItems(newItems);
		});
	};

	useEffect(loadItems, []);

	return (
		<SelectRaw
			value={value}
			onChange={onChange}
			isInvalid={isInvalid}
			htmlId={htmlId}
			items={items}
			idFieldName={idFieldName}
			labelFieldName={labelFieldName}
			assignObject={assignObject}
		/>
	);
};

/**
 * Tato komponenta slouží k výběru dokumentu z kolekce ve formuláři - automaticky načte celou kolekci z REST API.
 */
const SelectDocument = withForm()(SelectDocumentRaw);

export default SelectDocument;
