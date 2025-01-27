//
//  RedeemVoucherViewController.swift
//  MullvadVPN
//
//  Created by Andreas Lif on 2022-08-05.
//  Copyright © 2022 Mullvad VPN AB. All rights reserved.
//

import MullvadREST
import MullvadTypes
import UIKit

protocol RedeemVoucherViewControllerDelegate: AnyObject {
    func redeemVoucherDidSucceed(
        _ controller: RedeemVoucherViewController,
        with response: REST.SubmitVoucherResponse
    )
    func redeemVoucherDidCancel(_ controller: RedeemVoucherViewController)
}

class RedeemVoucherViewController: UIViewController, UINavigationControllerDelegate {
    private let contentView = RedeemVoucherContentView()
    private var voucherTask: Cancellable?
    private var interactor: RedeemVoucherInteractor?

    weak var delegate: RedeemVoucherViewControllerDelegate?

    init(interactor: RedeemVoucherInteractor) {
        super.init(nibName: nil, bundle: nil)
        self.interactor = interactor
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override var preferredStatusBarStyle: UIStatusBarStyle {
        .lightContent
    }

    // MARK: - Life Cycle

    override func viewDidLoad() {
        super.viewDidLoad()
        configureUI()
        addActions()
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        enableEditing()
    }

    // MARK: - private functions

    private func enableEditing() {
        guard !contentView.isEditing else { return }
        contentView.isEditing = true
    }

    private func addActions() {
        contentView.redeemAction = { [weak self] code in
            self?.submit(code: code)
        }

        contentView.cancelAction = { [weak self] in
            self?.cancel()
        }
    }

    private func configureUI() {
        view.addSubview(contentView)
        view.addConstrainedSubviews([contentView]) {
            contentView.pinEdgesToSuperview(.all())
        }
    }

    private func submit(code: String) {
        contentView.state = .verifying
        voucherTask = interactor?.redeemVoucher(code: code, completion: { [weak self] result in
            guard let self else { return }
            switch result {
            case let .success(value):
                contentView.state = .success
                contentView.isEditing = false
                delegate?.redeemVoucherDidSucceed(self, with: value)
            case let .failure(error):
                contentView.state = .failure(error)
            }
        })
    }

    private func cancel() {
        contentView.isEditing = false

        voucherTask?.cancel()

        delegate?.redeemVoucherDidCancel(self)
    }
}
